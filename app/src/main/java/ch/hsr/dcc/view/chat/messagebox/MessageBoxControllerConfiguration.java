package ch.hsr.dcc.view.chat.messagebox;

import ch.hsr.dcc.application.MessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBoxControllerConfiguration {

    @Bean
    public MessageBoxController messageBoxController(MessageService messageService) {
        return new MessageBoxController(messageService);
    }
}
