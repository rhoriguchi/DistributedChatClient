package ch.hsr.dsa.event.messagereceived;

import ch.hsr.dsa.application.MessageService;
import ch.hsr.dsa.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageReceivedEventListenerConfiguration {

    @Bean
    public MessageReceiveEventListener messageEventListener(MessageService messageService,
                                                            UserService userService) {
        return new MessageReceiveEventListener(messageService, userService);
    }
}
