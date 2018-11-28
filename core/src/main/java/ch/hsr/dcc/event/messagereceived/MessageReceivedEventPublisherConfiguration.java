package ch.hsr.dcc.event.messagereceived;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageReceivedEventPublisherConfiguration {

    @Bean
    public MessageReceivedEventPublisher messageEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new MessageReceivedEventPublisher(applicationEventPublisher);
    }
}
