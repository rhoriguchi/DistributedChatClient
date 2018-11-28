package ch.hsr.dcc.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorBoxControllerConfiguration {

    @Bean
    public ErrorBoxController errorBoxController() {
        return new ErrorBoxController();
    }
}
