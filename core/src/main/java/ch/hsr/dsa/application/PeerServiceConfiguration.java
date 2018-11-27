package ch.hsr.dsa.application;

import ch.hsr.dsa.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class PeerServiceConfiguration {

    @Bean
    public PeerService peerService(PeerRepository peerRepository) {
        return new PeerService(peerRepository);
    }
}
