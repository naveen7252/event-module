package io.nuls.event;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Spring boot application class which bootstraps event-module for Nuls Blockchain
 * @author Naveen
 */
@SpringBootApplication
@EnableScheduling
public class EventModuleApp {

    @Value("${NULS.API.URL}")
    private String API_SERVER_URL;

    @Value("${THREAD.POOL.SIZE}")
    private int threadPoolSize;

    public static void main(String[] args) {
        SpringApplication.run(EventModuleApp.class, args);
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .rootUri(API_SERVER_URL);
    }

    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder().build();
    }

    @Bean()
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(threadPoolSize);
        return taskScheduler;
    }
}
