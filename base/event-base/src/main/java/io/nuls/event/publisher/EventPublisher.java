package io.nuls.event.publisher;

import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.event.service.EventService;
import io.nuls.kernel.model.Result;
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
            if(verifyBlockHeight(bestHeight)){
                publishBlockEvent(blockMap);
                response = eventService.getLatestBlock(bestHeight);
                System.out.println("block by height response :"+response.isSuccess());
                publishEventByTxType(response);
            }
        }
    }

    private  void  publishEventByTxType(Result blockResponse){
        if(blockResponse.isSuccess()){
            Map<String,Object> blockMap = (Map<String, Object>)blockResponse.getData();
            List<Map<String, Object>> txListMap = (List<Map<String, Object>>)blockMap.get("txList");
            for(Map txMap : txListMap){
                int txType = (Integer)txMap.get("type");
                List<Map<String, Object>> outputsList =  (List<Map<String, Object>>)txMap.get("outputs");
                switch (txType) {
                    case EventConstant.TX_TYPE_COINBASE:
                        // Do nothing?? or notify Consensus awardees ?
                        publishTxEvent(outputsList,EventResourceConstant.TX_COINBASE_SUBSCRIPTION);
                        break;
                    case EventConstant.TX_TYPE_TRANSFER:
                        publishTxEvent(outputsList,EventResourceConstant.TX_TRANSFER_SUBSCRIPTION);
                        break;
                    case EventConstant.TX_TYPE_YELLOW_PUNISH:
                        publishAgentEvent(outputsList,EventResourceConstant.AGENT_YELLOWCARD_SUBSCRIPTION);
                        break;
                    case EventConstant.TX_TYPE_RED_PUNISH:
                        publishAgentEvent(outputsList,EventResourceConstant.AGENT_REDCARD_SUBSCRIPTION);
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
            System.out.println("Published Block Event --> "+bestHeight);
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
            int amount = (Integer)map.get("value"); // remove later
            System.out.println("Address:"+address+"---> Amount:"+amount); // remove later
            this.template.convertAndSend(subscription+address, new SubscribableMessage(true,map));
        }
    }

    private void publishAgentEvent(List<Map<String, Object>> txOutPutList,String subscription){
            //get agent  details by tx hash ???
    }
}
