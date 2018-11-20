package ch.hsr.view.chat.statusbox;

import ch.hsr.application.PeerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBoxControllerConfiguration {

    @Bean
    public StatusBoxController statusBoxController(PeerService peerService) {
        return new StatusBoxController(peerService);
    }
}
