package ch.hsr.infrastructure.configuration;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessageQueHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PMessageQueHolderConfiguration {

    @Bean
    public TomP2PMessageQueHolder tomP2PMessageQueHolder(MessageReceivedEventPublisher messageReceivedEventPublisher) {
        return new TomP2PMessageQueHolder(messageReceivedEventPublisher);
    }
}
