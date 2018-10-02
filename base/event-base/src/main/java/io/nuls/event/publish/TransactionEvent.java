package io.nuls.event.publish;

import io.nuls.event.constant.EventConstant;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.SubscribableMessage;
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

    private int initialTxBlkHeight;

    /**
     * Publishes Transaction events based on transaction type.
     *
     */
    @Override
    public void publish() {
        Result result = getTxListFromBlock();
       if (result.isSuccess()){
           Map<String,Object> blockMap = (Map<String, Object>)result.getData();
           int height = (Integer) blockMap.get("height");
           if( height > initialTxBlkHeight){
               initialTxBlkHeight = height;
               List<Map<String, Object>> txMapList = (List<Map<String, Object>>)blockMap.get("txList");
               for (Map txMap : txMapList){
                   int type  = (Integer) txMap.get("type");
                   List<Map<String, Object>> outputsList =  (List<Map<String, Object>>)txMap.get("outputs");
                   if(type == EventConstant.TX_TYPE_TRANSFER){
                       publishTxEvent(outputsList, EventResourceConstant.TX_TRANSFER_SUBSCRIPTION);
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
            this.template.convertAndSend(subscription+address, new SubscribableMessage(true,map));
        }
    }
}
