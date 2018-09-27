package ch.hsr.view.configuration;

import ch.hsr.application.LoginService;
import ch.hsr.view.LoginController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginControllerConfiguration {

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}
