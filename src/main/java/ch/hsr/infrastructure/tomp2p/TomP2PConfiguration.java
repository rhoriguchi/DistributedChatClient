package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.message.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PConfiguration {

    @Bean
    public TomP2P tomP2P(PeerHolder peerHolder, DHTHandler dhtHandler, MessageHandler messageHandler) {
        return new TomP2PImplementation(peerHolder, dhtHandler, messageHandler);
    }
}
