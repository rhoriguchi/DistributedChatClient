package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.DHTHandler;
import ch.hsr.infrastructure.tomp2p.PeerHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DHTHandlerConfiguration {

    @Bean
    public DHTHandler dhtHandler(PeerHolder peerHolder) {
        return new DHTHandler(peerHolder);
    }
}
