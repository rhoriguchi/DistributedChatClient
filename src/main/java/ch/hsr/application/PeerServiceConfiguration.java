package ch.hsr.application;

import ch.hsr.mapping.keystore.KeyStoreRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerServiceConfiguration {

    @Bean
    public PeerService peerService(PeerRepository peerRepository, KeyStoreRepository keyStoreRepository) {
        return new PeerService(peerRepository, keyStoreRepository);
    }
}
