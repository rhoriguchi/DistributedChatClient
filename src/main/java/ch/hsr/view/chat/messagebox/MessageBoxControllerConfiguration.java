package ch.hsr.view.chat.messagebox;

import ch.hsr.application.MessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBoxControllerConfiguration {

    @Bean
    public MessageBoxController messageBoxController(MessageService messageService) {
        return new MessageBoxController(messageService);
    }
}
