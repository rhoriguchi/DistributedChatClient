package ch.hsr.view.chat.statusbox;

import ch.hsr.application.UserService;
import ch.hsr.view.chat.peerbox.PeerBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBoxControllerConfiguration {

    @Bean
    public StatusBoxController statusBoxController(UserService userService) {
        return new StatusBoxController(userService);
    }
}
