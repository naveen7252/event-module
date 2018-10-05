package io.nuls.event.publish;

import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.event.model.TransactionData;
import io.nuls.kernel.model.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Publishes Transaction related events.
 * Events are categorized based on transaction type
 */
@Component
public class TransactionEvent extends AbstractNulsEvent {

    /**
     * Publishes Transaction events based on transaction type.
     * For Coinbbase reward subscription /tx/receiveReward/{Nuls Address}
     * For Transfer token subscription /tx/receiveToken/{Nuls Address}
     */
    @Override
    public void publish() {
        Result result = getBlockWithTransactions();
       if (result.isSuccess()){
           Map<String,Object> blockMap = (Map<String, Object>)result.getData();
           int height = (Integer) blockMap.get("height");
           if(checkBlockHeight(height)){
               List<Map<String, Object>> txMapList = (List<Map<String, Object>>)blockMap.get("txList");
               for (Map txMap : txMapList){
                   int type  = (Integer) txMap.get("type");
                   System.out.println("TRANSACTION:::: Height :"+height+" TYPE :"+type+" initialHeight:"+localHeight);
                   List<Map<String, Object>> outputsList =  (List<Map<String, Object>>)txMap.get("outputs");
                   if(type == EventConstant.TX_TYPE_TRANSFER){
                       publishTxEvent(outputsList, EventResourceConstant.TOKEN_RECEIVE_SUBSCRIPTION);
                   }else if(type == EventConstant.TX_TYPE_COINBASE){
                       publishTxEvent(outputsList, EventResourceConstant.TX_COINBASE_SUBSCRIPTION);
                   }
               }
           }
       }
    }


    private void publishTxEvent(List<Map<String, Object>> txOutPutList,String subscription){
        for(Map<String, Object> map : txOutPutList){
            String address = (String)map.get("address");
            TransactionData data = new TransactionData();
            data.setAddress(address);
            data.setNulsValue((Integer)map.get("value"));
            this.template.convertAndSend(subscription+address, new SubscribableMessage(true,data));
        }
    }
}
