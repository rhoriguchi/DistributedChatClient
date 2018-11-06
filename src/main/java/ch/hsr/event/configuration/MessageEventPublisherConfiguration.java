package ch.hsr.event.configuration;

import ch.hsr.event.message.MessageReceivedEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageEventPublisherConfiguration {

    @Bean
    public MessageReceivedEventPublisher messageEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new MessageReceivedEventPublisher(applicationEventPublisher);
    }
}
