package ch.hsr.event.configuration;

import ch.hsr.application.MessageService;
import ch.hsr.event.message.MessageReceiveEventListener;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageEventListenerConfiguration {

    @Bean
    public MessageReceiveEventListener messageEventListener(MessageBoxController messageBoxController,
                                                            MessageService messageService) {
        return new MessageReceiveEventListener(messageBoxController, messageService);
    }
}
