package ch.hsr.dsa.application.listener;

import ch.hsr.dsa.application.MessageService;
import ch.hsr.dsa.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageReceivedEventListenerConfiguration {

    @Bean
    public MessageReceivedEventListener messageReceivedEventListener(MessageService messageService,
                                                                     UserService userService) {
        return new MessageReceivedEventListener(messageService, userService);
    }
}
