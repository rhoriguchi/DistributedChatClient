package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.MessageHandler;
import ch.hsr.infrastructure.tomp2p.PeerHolder;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.TomP2PImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PConfiguration {

    @Bean
    public TomP2P tomP2P(PeerHolder peerHolder, MessageHandler messageHandler) {
        return new TomP2PImplementation(peerHolder, messageHandler);
    }
}
