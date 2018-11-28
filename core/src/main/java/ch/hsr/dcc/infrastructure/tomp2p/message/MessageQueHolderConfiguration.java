package ch.hsr.dcc.infrastructure.tomp2p.message;

import ch.hsr.dcc.event.messagereceived.MessageReceivedEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueHolderConfiguration {

    @Bean
    public MessageQueHolder messageQueHolder(MessageReceivedEventPublisher messageReceivedEventPublisher) {
        return new MessageQueHolder(messageReceivedEventPublisher);
    }
}
