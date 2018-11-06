package ch.hsr.infrastructure.configuration;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import ch.hsr.infrastructure.tomp2p.MessageHandler;
import ch.hsr.infrastructure.tomp2p.PeerHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageHandlerConfiguration {

    @Bean
    public MessageHandler messageHandler(MessageReceivedEventPublisher messageReceivedEventPublisher, PeerHolder peerHolder) {
        return new MessageHandler(messageReceivedEventPublisher, peerHolder);
    }
}
