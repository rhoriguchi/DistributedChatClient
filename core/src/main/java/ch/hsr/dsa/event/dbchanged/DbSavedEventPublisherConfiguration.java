package ch.hsr.dsa.event.dbchanged;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbSavedEventPublisherConfiguration {

    @Bean
    public DbSavedEventPublisher dbSavedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DbSavedEventPublisher(applicationEventPublisher);
    }
}
