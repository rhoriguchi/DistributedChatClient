package ch.hsr.dsa.view.listener;

import ch.hsr.dsa.view.LoginController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginEventListenerConfiguration {

    @Bean
    public LoginEventListener loginEventListener(LoginController loginController) {
        return new LoginEventListener(loginController);
    }
}
