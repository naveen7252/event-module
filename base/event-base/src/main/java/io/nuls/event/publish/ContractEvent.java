package io.nuls.event.publish;

import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.ContractData;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.event.model.TransactionData;
import io.nuls.kernel.model.Result;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates Contract related events
 *  <li>Contract Creation Event</li> - Notified to contract creator
 *  <li>Contract Call Event (transfer)</li> - Notify token receive in case of transfer
 */
@Component
public class ContractEvent extends AbstractNulsEvent {

    /**
     *  Subscription for contract creation - /contract/create/{creator address}
     *  Subscription for Token receive  - /tx/receiveToken/{Nuls Address}
     */
    @Override
    public void publish() {
        Result result = getBlockWithTransactions();
        if(result.isSuccess()){
            Map<String,Object> blockMap = (Map<String, Object>)result.getData();
            int height = (Integer) blockMap.get("height");
            if(checkBlockHeight(height)) {
                List<Map<String, Object>> txMapList = (List<Map<String, Object>>) blockMap.get("txList");
                for (Map<String,Object> txMap : txMapList){
                    int type = (Integer)txMap.get("type");
                    String hash = (String) txMap.get("hash");
                    switch (type){
                        case EventConstant.TX_TYPE_CREATE_CONTRACT:
                            publishContractEvent(EventResourceConstant.CONTRACT_CREATE_SUBSCRIPTION,eventService.getContractTxByHash(hash));
                            break;
                        case EventConstant.TX_TYPE_CALL_CONTRACT:
                            publishContractEvent(EventResourceConstant.TOKEN_RECEIVE_SUBSCRIPTION,eventService.getContractTxByHash(hash));
                            break;
                        case EventConstant.TX_TYPE_DELETE_CONTRACT:
                            break;
                    }
                }
            }
        }
    }

    private void publishContractEvent(String subscription,Result result){
        if(result.isSuccess()){
            Map<String,Object> map = (Map<String,Object>)result.getData();
            Map<String,Object> contractResult = (Map<String,Object>)map.get("contractResult");
            List<Map<String,Object>> tokenTransfers = (List<Map<String,Object>>)contractResult.get("tokenTransfers");
            ContractData contractData = new ContractData();
            contractData.setTime((Long)map.get("time"));
            contractData.setBlockHeight((Integer)map.get("blockHeight"));
            contractData.setTxHash((String)map.get("hash"));
            contractData.setContractAddress((String)contractResult.get("contractAddress"));
            contractData.setSymbol((String)contractResult.get("symbol"));
            contractData.setTokenName((String)contractResult.get("name"));
            contractData.setRemark((String)contractResult.get("remark"));
            for(Map<String,Object> tokenTransferMap : tokenTransfers){
                String sender = (String)tokenTransferMap.get("from");
                String receiver = (String)tokenTransferMap.get("to");
                contractData.setSender(sender);
                contractData.setReceiver(receiver);
                contractData.setTokenValue((String)tokenTransferMap.get("value"));
                System.out.println("Subscription-->"+subscription+receiver+" contractData :"+contractData);
                this.template.convertAndSend(subscription+receiver,new SubscribableMessage(true,new TransactionData(contractData)));
            }
        }
    }
}
