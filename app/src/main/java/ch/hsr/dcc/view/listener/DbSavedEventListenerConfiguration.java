package ch.hsr.dcc.view.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbSavedEventListenerConfiguration {

    @Bean
    public DbSavedEventListener dbSavedEventListener() {
        return new DbSavedEventListener();
    }
}
