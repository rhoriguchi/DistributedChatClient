package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.mapping.peer.PeerMapper;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerRepositoryConfiguration {

    @Bean
    public PeerRepository peerRepository(TomP2P tomP2P) {
        return new PeerMapper(tomP2P);
    }
}
