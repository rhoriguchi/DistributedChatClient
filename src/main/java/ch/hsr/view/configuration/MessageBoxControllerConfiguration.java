package ch.hsr.view.configuration;

import ch.hsr.application.MessageService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBoxControllerConfiguration {

    @Bean
    public MessageBoxController messageBoxController(MessageService messageService) {
        return new MessageBoxController(messageService);
    }
}
