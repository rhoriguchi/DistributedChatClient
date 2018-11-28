package ch.hsr.dcc.view.chat.statusbox;

import ch.hsr.dcc.application.PeerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBoxControllerConfiguration {

    @Bean
    public StatusBoxController statusBoxController(PeerService peerService) {
        return new StatusBoxController(peerService);
    }
}
