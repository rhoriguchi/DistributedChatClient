package ch.hsr.dcc.view;

import ch.hsr.dcc.application.PeerService;
import ch.hsr.dcc.view.chat.friendsbox.FriendsBoxController;
import ch.hsr.dcc.view.chat.statusbox.StatusBoxController;
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
