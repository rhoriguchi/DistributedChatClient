package ch.hsr.application.configuration;

import ch.hsr.application.PeerService;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerServiceConfiguration {

    @Bean
    public PeerService loginService(PeerRepository peerRepository) {
        return new PeerService(peerRepository);
    }
}
