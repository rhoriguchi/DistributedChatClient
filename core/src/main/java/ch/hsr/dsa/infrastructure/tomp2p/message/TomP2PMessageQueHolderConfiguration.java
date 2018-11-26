package ch.hsr.dsa.infrastructure.tomp2p.message;

import ch.hsr.dsa.event.messagereceived.MessageReceivedEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PMessageQueHolderConfiguration {

    @Bean
    public TomP2PMessageQueHolder tomP2PMessageQueHolder(MessageReceivedEventPublisher messageReceivedEventPublisher) {
        return new TomP2PMessageQueHolder(messageReceivedEventPublisher);
    }
}
