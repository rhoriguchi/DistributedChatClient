package ch.hsr.application.configuration;

import ch.hsr.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {

    @Bean
    public UserService loginService() {
        return new UserService();
    }
}
