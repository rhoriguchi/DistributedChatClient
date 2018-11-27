package ch.hsr.dsa.event.login;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginEventPublisherConfiguration {

    @Bean
    public LoginEventPublisher loginEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new LoginEventPublisher(applicationEventPublisher);
    }
}
