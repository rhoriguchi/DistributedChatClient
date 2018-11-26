package ch.hsr.dsa.infrastructure.tomp2p.message;

import ch.hsr.dsa.infrastructure.tomp2p.PeerHolder;
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
