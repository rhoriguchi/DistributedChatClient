package ch.hsr.application.configuration;

import ch.hsr.application.MessageService;
import ch.hsr.mapping.message.MessageRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceConfiguration {

    @Bean
    public MessageService messageService(MessageRepository messageRepository, PeerRepository peerRepository) {
        return new MessageService(messageRepository, peerRepository);
    }
}
