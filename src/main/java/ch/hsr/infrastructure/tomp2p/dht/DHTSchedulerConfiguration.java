package ch.hsr.infrastructure.tomp2p.dht;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class DHTSchedulerConfiguration {

    @Bean
    public DHTScheduler DHTScheduler(DHTHandler dhtHandler) {
        return new DHTScheduler(dhtHandler);
    }
}
