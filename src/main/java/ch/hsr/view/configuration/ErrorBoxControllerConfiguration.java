package ch.hsr.view.configuration;

import ch.hsr.view.ErrorBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorBoxControllerConfiguration {

    @Bean
    public ErrorBoxController errorBoxController() {
        return new ErrorBoxController();
    }
}
