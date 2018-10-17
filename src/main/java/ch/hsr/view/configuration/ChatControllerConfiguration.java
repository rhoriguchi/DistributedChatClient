package ch.hsr.view.configuration;

import ch.hsr.view.chat.ChatController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatControllerConfiguration {

    @Bean
    public ChatController chatController() {
        return new ChatController();
    }
}
