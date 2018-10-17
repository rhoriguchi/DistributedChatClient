package ch.hsr.view.configuration;

import ch.hsr.application.PeerService;
import ch.hsr.view.LoginController;
import ch.hsr.view.RootController;
import ch.hsr.view.chat.statusbox.StatusBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginControllerConfiguration {

    @Bean
    public LoginController loginController(RootController rootController,
                                           StatusBoxController statusBoxController,
                                           PeerService peerService) {
        return new LoginController(rootController, statusBoxController, peerService);
    }
}
