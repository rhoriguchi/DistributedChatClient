package ch.hsr.view.configuration;

import ch.hsr.view.RootController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootControllerConfiguration {

    @Bean
    public RootController rootController() {
        return new RootController();
    }
}
