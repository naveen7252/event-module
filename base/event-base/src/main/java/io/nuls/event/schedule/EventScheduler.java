package io.nuls.event.schedule;

import io.nuls.event.publish.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring scheduler class which schedules the tasks to generate events.
 * Each event is scheduled as separate task.
 * @author Naveen(naveen.balamuri@gmail.com)
 */
@Component
public class EventScheduler {

    @Autowired
    private NewBlockEvent newBlockEvent;

    @Autowired
    private TransactionEvent transactionEvent;

    @Autowired
    private AgentPunishEvent  agentPunishEvent;

    @Autowired
    private ContractEvent contractEvent;

    @Autowired
    private EventEmitter eventEmitter;

    /**
     * Task to generate Nuls new block event. Taks is scheduled to call for every fixedDelay milli seconds
     */
    @Scheduled(fixedDelay = 3000)
    public void publishNewBlock(){
        eventEmitter.emit(newBlockEvent);
    }

    /**
     * Task to generate Nuls Transaction event. Taks is scheduled to call for every fixedDelay milli seconds
     * Event is generated based on transaction type.
     */
    @Scheduled(fixedDelay = 2000)
    public void publishTransactionEvent(){
        eventEmitter.emit(transactionEvent);
    }

    /**
     * Task to generate Nuls Agent punish event. Taks is scheduled to call for every fixedDelay milli seconds
     *  Based on transaction type, generates Yellow or Red card event
     */
    @Scheduled(fixedDelay = 2000)
    public void publishAgentPunishEvent(){
        eventEmitter.emit(agentPunishEvent);
    }

    /**
     * Scheduled task to generate Contract related events
     */
    @Scheduled(fixedDelay = 2000)
    public void publishContractEvent(){eventEmitter.emit(contractEvent);}
}
