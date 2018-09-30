package io.nuls.event.schedule;

import io.nuls.event.publish.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class EventScheduler {

    @Autowired
    private EventPublisher eventPublisher;

    @Scheduled(fixedDelay = 1000)
    public void scheduleNulsEvent(){
        eventPublisher.publishNulsEvents();

    }
}
