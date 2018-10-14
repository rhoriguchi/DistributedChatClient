package ch.hsr.view.chat.messagebox;

import ch.hsr.application.MessageService;
import ch.hsr.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBoxControllerConfiguration {

    @Bean
    public MessageBoxController messageBoxController(MessageService messageService,
                                                     UserService userService) {
        return new MessageBoxController(messageService, userService);
    }
}
