package ch.hsr.infrastructure.tomp2p;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DHTHandlerConfiguration {

    @Bean
    public DHTHandler dhtHandler(PeerHolder peerHolder) {
        return new DHTHandler(peerHolder);
    }
}
