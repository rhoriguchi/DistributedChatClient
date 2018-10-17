package ch.hsr.view.configuration;

import ch.hsr.application.PeerService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import ch.hsr.view.chat.peerbox.PeerBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerBoxControllerConfiguration {

    @Bean
    public PeerBoxController peerBoxController(MessageBoxController messageBoxController, PeerService peerService) {
        return new PeerBoxController(messageBoxController, peerService);
    }
}
