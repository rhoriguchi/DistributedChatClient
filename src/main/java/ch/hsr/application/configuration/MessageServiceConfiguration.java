package ch.hsr.application.configuration;

import ch.hsr.application.MessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceConfiguration {

    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
