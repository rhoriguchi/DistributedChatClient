package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.PeerHolder;
import ch.hsr.infrastructure.tomp2p.message.MessageHandler;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessageQueHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageHandlerConfiguration {

    @Bean
    public MessageHandler messageHandler(PeerHolder peerHolder,
                                         TomP2PMessageQueHolder tomP2PMessageQueHolder) {
        return new MessageHandler(peerHolder, tomP2PMessageQueHolder);
    }
}
