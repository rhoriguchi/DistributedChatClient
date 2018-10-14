package ch.hsr.view.chat.peerbox;

import ch.hsr.application.UserService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerBoxControllerConfiguration {

    @Bean
    public PeerBoxController peerBoxController(MessageBoxController messageBoxController, UserService userService) {
        return new PeerBoxController(messageBoxController, userService);
    }
}
