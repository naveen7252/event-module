package io.nuls.event.publish;

import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.event.util.EventUtil;
import io.nuls.kernel.model.Result;
import io.nuls.kernel.model.Transaction;
import io.nuls.kernel.model.TransactionLogicData;
import io.nuls.kernel.utils.AddressTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Publishes Agent punish events
 */
@Component
public class AgentPunishEvent extends AbstractNulsEvent {

    /**
     * Publish agent punish event based on transaction type.
     *  Agent with their address can subscribe these events
     *  For Yellow Card subscribe to /agent/yellowCard/{Agent_address}
     *  For RED Card subscribe to /agent/redCard/{Agent_address}
     */
    @Override
    public void publish() {
        Result result =  getBlockWithTransactions();
        if(result.isSuccess()){
            Map<String,Object> blockMap = (Map<String, Object>)result.getData();
            int height = (Integer) blockMap.get("height");
            ///System.out.println("height Agent :"+height);
            if(checkBlockHeight(height)){
                List<Map<String, Object>> txMapList = (List<Map<String, Object>>)blockMap.get("txList");
                for(Map<String,Object> map : txMapList){
                    int type = (Integer)map.get("type");
                    System.out.println("AGENT::::: Height :"+height+" TYPE :"+type+" initialHeight:"+localHeight);
                    if (type == EventConstant.TX_TYPE_YELLOW_PUNISH){
                        publishAgentPunishEvent(map, EventResourceConstant.AGENT_YELLOWCARD_SUBSCRIPTION);
                    }else if(type == EventConstant.TX_TYPE_RED_PUNISH){
                        publishAgentPunishEvent(map, EventResourceConstant.AGENT_REDCARD_SUBSCRIPTION);
                    }
                }
            }
        }
    }

    private void publishAgentPunishEvent(Map<String,Object> txMap,String subscription){
        int type = (Integer) txMap.get("type");
        long time = (Long)txMap.get("time");
        String hash = (String)txMap.get("hash");
        Result result = eventService.getTxByHash(hash);
        if(result.isSuccess() && result.getData() != null){
            Map<String,Object> dataMap = (Map<String,Object>)result.getData();
            Transaction tx = EventUtil.parseTransaction(type,(String)dataMap.get("value"));
            TransactionLogicData data = tx.getTxData();
            for(byte[] addressByte : data.getAddresses()){
                String agentAddress = AddressTool.getStringAddressByBytes(addressByte);
                AgentPunishDTO dto = new AgentPunishDTO.AgentPunishDTOBuilder(agentAddress,type,time).build();
                this.template.convertAndSend(subscription+agentAddress,new SubscribableMessage(true,dto));
            }
        }
    }
}
