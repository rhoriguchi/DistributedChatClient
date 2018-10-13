package ch.hsr.view.chat.peerbox;

import ch.hsr.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerBoxControllerConfiguration {

    @Bean
    public PeerBoxController peerBoxController(UserService userService) {
        return new PeerBoxController(userService);
    }
}
