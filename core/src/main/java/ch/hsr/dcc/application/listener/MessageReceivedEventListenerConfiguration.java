package ch.hsr.dcc.application.listener;

import ch.hsr.dcc.application.GroupService;
import ch.hsr.dcc.application.MessageService;
import ch.hsr.dcc.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageReceivedEventListenerConfiguration {

    @Bean
    public MessageReceivedEventListener messageReceivedEventListener(MessageService messageService,
                                                                     UserService userService,
                                                                     GroupService groupService) {
        return new MessageReceivedEventListener(messageService, userService, groupService);
    }
}
