package ch.hsr.application.configuration;

import ch.hsr.application.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginServiceConfiguration {

    @Bean
    public LoginService loginService() {
        return new LoginService();
    }
}
