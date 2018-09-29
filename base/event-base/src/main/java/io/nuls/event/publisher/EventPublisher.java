package io.nuls.event.publisher;

import io.nuls.core.tools.log.Log;
import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.event.service.EventService;
import io.nuls.event.util.EventUtil;
import io.nuls.kernel.model.CoinData;
import io.nuls.kernel.model.Result;
import io.nuls.kernel.model.Transaction;
import io.nuls.kernel.model.TransactionLogicData;
import io.nuls.kernel.utils.AddressTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

@Configuration
public class EventPublisher {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private EventService eventService;

    private int initialHeight;

    @Scheduled(fixedDelay = 1000)
    public void publishNulsEvents(){
        Result response = eventService.getLatestBlock();
        if(response.isSuccess()){
            Map<String,Object> blockMap = (Map<String, Object>)response.getData();
            int bestHeight = (Integer)blockMap.get("height");
            //bestHeight = 84035;
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
                System.out.println("Transaction type  -> "+txType);
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
        System.out.println("Block Height:"+bestHeight);
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
        System.out.println("Event Subscription: "+subscription);
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
            Transaction tx = EventUtil.getTransaction(type,(String)dataMap.get("value"));
            TransactionLogicData  data = tx.getTxData();
            for(byte[] addressByte : data.getAddresses()){
                String agentAddress = AddressTool.getStringAddressByBytes(addressByte);
                System.out.println("Yellow Card for Address:"+ agentAddress);
                AgentPunishDTO dto = new AgentPunishDTO.AgentPunishDTOBuilder(agentAddress,type,time).build();
                this.template.convertAndSend(subscription+agentAddress,new SubscribableMessage(true,dto));
            }
        }
    }
}
