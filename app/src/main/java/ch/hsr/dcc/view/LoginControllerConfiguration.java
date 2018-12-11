package ch.hsr.dcc.view;

import ch.hsr.dcc.application.PeerService;
import ch.hsr.dcc.view.chat.friendsbox.FriendGroupBoxController;
import ch.hsr.dcc.view.chat.statusbox.StatusBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginControllerConfiguration {

    @Bean
    public LoginController loginController(RootController rootController,
                                           StatusBoxController statusBoxController,
                                           FriendGroupBoxController friendGroupBoxController,
                                           PeerService peerService) {
        return new LoginController(rootController, statusBoxController, friendGroupBoxController, peerService);
    }
}
