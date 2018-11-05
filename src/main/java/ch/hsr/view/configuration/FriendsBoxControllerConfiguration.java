package ch.hsr.view.configuration;

import ch.hsr.application.PeerService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import ch.hsr.view.chat.friendsbox.FriendsBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendsBoxControllerConfiguration {

    @Bean
    public FriendsBoxController friendsBoxController(MessageBoxController messageBoxController, PeerService peerService) {
        return new FriendsBoxController(messageBoxController, peerService);
    }
}
