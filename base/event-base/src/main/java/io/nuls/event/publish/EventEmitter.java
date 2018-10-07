package io.nuls.event.publish;

import org.springframework.stereotype.Component;

/**
 * To generate events
 * @author Naveen(naveen.balamuri@gmail.com)
 */
@Component
public class EventEmitter {

    /**
     * Emit the event
     * @param nulsEvent
     */
    public void emit(NulsEvent nulsEvent){
        nulsEvent.publish();
    }
}
