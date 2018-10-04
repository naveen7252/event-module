package io.nuls.event.publish;

import io.nuls.event.service.EventService;
import io.nuls.kernel.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Map;

/**
 *  Abstract class for all Nuls events calss. Every new event class will extend this class
 */
public abstract class AbstractNulsEvent implements NulsEvent {

    @Autowired
    protected EventService eventService;

    @Autowired
    protected SimpMessagingTemplate template;

    protected int localHeight;

    /**
     *  Gets latest block
     * @return Result
     */
    protected Result getLatestBlock() {
        return eventService.getLatestBlock();
    }

    /**
     *  Gets latest block with underlying transaction from Nuls Blockchain
     * @return Result
     */
    protected Result getTxListFromBlock(){
        Result result = getLatestBlock();
        if(result.isSuccess()){
            Map<String,Object> blockData = (Map<String,Object>)result.getData();
            int height = (Integer)blockData.get("height");
            result  =  eventService.getBlockByHeight(height);
            if(result.isSuccess()){
                return result;
            }
        }
        return null;
    }

    protected boolean checkBlockHeight(int height){
        if(height > localHeight){
            localHeight = height;
            return true;
        }
        return false;
    }
}
