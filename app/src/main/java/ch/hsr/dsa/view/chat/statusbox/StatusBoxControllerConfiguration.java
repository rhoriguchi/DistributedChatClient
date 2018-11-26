package ch.hsr.dsa.view.chat.statusbox;

import ch.hsr.dsa.application.PeerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBoxControllerConfiguration {

    @Bean
    public StatusBoxController statusBoxController(PeerService peerService) {
        return new StatusBoxController(peerService);
    }
}
