package ch.hsr.view.configuration;

import ch.hsr.application.PeerService;
import ch.hsr.view.chat.statusbox.StatusBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBoxControllerConfiguration {

    @Bean
    public StatusBoxController statusBoxController(PeerService peerService) {
        return new StatusBoxController(peerService);
    }
}
