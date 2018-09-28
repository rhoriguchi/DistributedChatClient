package ch.hsr.view.configuration;

import ch.hsr.application.UserService;
import ch.hsr.view.LoginController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginControllerConfiguration {

    @Bean
    public LoginController loginController(UserService userService) {
        return new LoginController(userService);
    }
}
