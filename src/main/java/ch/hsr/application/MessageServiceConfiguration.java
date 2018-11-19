package ch.hsr.application;

import ch.hsr.application.MessageService;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.message.MessageRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceConfiguration {

    @Bean
    public MessageService messageService(MessageRepository messageRepository,
                                         GroupRepository groupRepository,
                                         PeerRepository peerRepository) {
        return new MessageService(messageRepository, groupRepository, peerRepository);
    }
}
