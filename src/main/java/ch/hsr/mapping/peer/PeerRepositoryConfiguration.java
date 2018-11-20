package ch.hsr.mapping.peer;

import ch.hsr.infrastructure.tomp2p.TomP2P;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerRepositoryConfiguration {

    @Bean
    public PeerRepository peerRepository(TomP2P tomP2P) {
        return new PeerMapper(tomP2P);
    }
}
