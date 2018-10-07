package io.nuls.event.publish;

import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.SubscribableMessage;
import io.nuls.kernel.model.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Publishes Nuls new block event
 * Event is published to Websocket end point. Clients can subscribe for new block event to this end point
 * For New Block subscription, /block/latest
 * @author Naveen(naveen.balamuri@gmail.com)
 */
@Component
public class NewBlockEvent extends AbstractNulsEvent {

    /**
     * Publishes new block event
     */
    @Override
    public void publish() {
        Result result = getLatestBlock();
        if(result.isSuccess()){
            Map<String,Object> data = (Map<String,Object>) result.getData();
            int height = (Integer)data.get("height");
            if(checkBlockHeight(height)){
                System.out.println("NEW BLOCK :::: Height :"+height+" initialHeight:"+localHeight);
                this.template.convertAndSend(EventResourceConstant.NEW_BLOCK_SUBSCRIPTION, new SubscribableMessage(true,result.getData()));
            }
        }
    }
}
