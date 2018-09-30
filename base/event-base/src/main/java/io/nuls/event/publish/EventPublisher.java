package io.nuls.event.publish;

import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.event.service.EventService;
import io.nuls.event.util.EventUtil;
import io.nuls.kernel.model.Result;
import io.nuls.kernel.model.Transaction;
import io.nuls.kernel.model.TransactionLogicData;
import io.nuls.kernel.utils.AddressTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EventPublisher {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private EventService eventService;

    private int initialHeight;

    public void publishNulsEvents(){
        Result response = eventService.getLatestBlock();
        if(response.isSuccess()){
            Map<String,Object> blockMap = (Map<String, Object>)response.getData();
            int bestHeight = (Integer)blockMap.get("height");
            if(verifyBlockHeight(bestHeight)){
                publishBlockEvent(blockMap);
                response = eventService.getBlockByHeight(bestHeight);
                publishEventByTxType(response);
                //publishAgentEvent(bestHeight);
            }
        }
    }

    private void publishEventByTxType(Result blockResponse){
        if(blockResponse.isSuccess()){
            Map<String,Object> blockMap = (Map<String, Object>)blockResponse.getData();
            List<Map<String, Object>> txListMap = (List<Map<String, Object>>)blockMap.get("txList");
            for(Map txMap : txListMap){
                int txType = (Integer)txMap.get("type");
                String txHash = (String)txMap.get("hash");
                List<Map<String, Object>> outputsList =  (List<Map<String, Object>>)txMap.get("outputs");
                switch (txType) {
                    case EventConstant.TX_TYPE_COINBASE:
                        publishTxEvent(outputsList,EventResourceConstant.TX_COINBASE_SUBSCRIPTION);
                        break;
                    case EventConstant.TX_TYPE_TRANSFER:
                        publishTxEvent(outputsList,EventResourceConstant.TX_TRANSFER_SUBSCRIPTION);
                        break;
                    case EventConstant.TX_TYPE_YELLOW_PUNISH:
                        publishAgentEvent(txMap,txHash,EventResourceConstant.AGENT_YELLOWCARD_SUBSCRIPTION);
                        break;
                    case EventConstant.TX_TYPE_RED_PUNISH:
                        publishAgentEvent(txMap,txHash,EventResourceConstant.AGENT_REDCARD_SUBSCRIPTION);
                    default:
                        System.out.println("Transaction type not supported -> "+txType);
                        break;
                }
            }
        }
    }
    private boolean verifyBlockHeight(int bestHeight){
        if(bestHeight > initialHeight){
            this.initialHeight = bestHeight;
            return true;
        }
        return false;
    }

    private void publishBlockEvent(Map<String,Object> map){
        this.template.convertAndSend(EventResourceConstant.NEW_BLOCK_SUBSCRIPTION, new SubscribableMessage(true,map));
    }

    private void publishTxEvent(List<Map<String, Object>> txOutPutList,String subscription){
        for(Map<String, Object> map : txOutPutList){
            String address = (String)map.get("address");
            this.template.convertAndSend(subscription+address, new SubscribableMessage(true,map));
        }
    }

    private void publishAgentEvent(int blockHeight){
       Result result = eventService.getAgentPunish(blockHeight);
       if(result.isSuccess()){
           List<AgentPunishDTO> punishDTOS = (List<AgentPunishDTO>) result.getData();
           for(AgentPunishDTO dto : punishDTOS){
               if(dto.getPunishType().equals(EventResourceConstant.RED)){
                   this.template.convertAndSend(EventResourceConstant.AGENT_REDCARD_SUBSCRIPTION+dto.getAgentAddress(),new SubscribableMessage(true,dto));
               }else if(dto.getPunishType().equals(EventResourceConstant.YELLOW)){
                   this.template.convertAndSend(EventResourceConstant.AGENT_YELLOWCARD_SUBSCRIPTION+dto.getAgentAddress(),new SubscribableMessage(true,dto));
               }
           }
       }
    }

    private void publishAgentEvent(Map<String,Object> txMap,String hash,String subscription){
        int type = (Integer) txMap.get("type");
        long time = (Long)txMap.get("time");
        Result result = eventService.getTxByHash(hash);
        if(result.isSuccess() && result.getData() != null){
            Map<String,Object> dataMap = (Map<String,Object>)result.getData();
            Transaction tx = EventUtil.parseTransaction(type,(String)dataMap.get("value"));
            TransactionLogicData  data = tx.getTxData();
            for(byte[] addressByte : data.getAddresses()){
                String agentAddress = AddressTool.getStringAddressByBytes(addressByte);
                AgentPunishDTO dto = new AgentPunishDTO.AgentPunishDTOBuilder(agentAddress,type,time).build();
                this.template.convertAndSend(subscription+agentAddress,new SubscribableMessage(true,dto));
            }
        }
    }
}
