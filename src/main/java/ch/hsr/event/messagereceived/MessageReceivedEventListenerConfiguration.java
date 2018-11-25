package ch.hsr.event.messagereceived;

import ch.hsr.application.MessageService;
import ch.hsr.application.UserService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageReceivedEventListenerConfiguration {

    @Bean
    public MessageReceiveEventListener messageEventListener(MessageBoxController messageBoxController,
                                                            MessageService messageService,
                                                            UserService userService) {
        return new MessageReceiveEventListener(messageBoxController, messageService, userService);
    }
}
