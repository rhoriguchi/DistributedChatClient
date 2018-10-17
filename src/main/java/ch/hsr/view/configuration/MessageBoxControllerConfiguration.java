package ch.hsr.view.configuration;

import ch.hsr.application.MessageService;
import ch.hsr.application.PeerService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBoxControllerConfiguration {

    @Bean
    public MessageBoxController messageBoxController(MessageService messageService,
                                                     PeerService peerService) {
        return new MessageBoxController(messageService, peerService);
    }
}
