package ch.hsr.dcc.application;

import ch.hsr.dcc.event.login.LoginEventPublisher;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class PeerServiceConfiguration {

    @Bean
    public PeerService peerService(PeerRepository peerRepository, LoginEventPublisher loginEventPublisher) {
        return new PeerService(peerRepository, loginEventPublisher);
    }
}
