package ch.hsr.dsa.mapping.peer;

import ch.hsr.dsa.infrastructure.tomp2p.TomP2P;
import ch.hsr.dsa.mapping.keystore.KeyStoreRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerRepositoryConfiguration {

    @Bean
    public PeerRepository peerRepository(TomP2P tomP2P, KeyStoreRepository keyStoreRepository) {
        return new PeerMapper(tomP2P, keyStoreRepository);
    }
}
