package ch.hsr.dsa.view;

import ch.hsr.dsa.application.PeerService;
import ch.hsr.dsa.view.chat.friendsbox.FriendsBoxController;
import ch.hsr.dsa.view.chat.statusbox.StatusBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginControllerConfiguration {

    @Bean
    public LoginController loginController(RootController rootController,
                                           StatusBoxController statusBoxController,
                                           FriendsBoxController friendsBoxController,
                                           PeerService peerService) {
        return new LoginController(rootController, statusBoxController, friendsBoxController, peerService);
    }
}
