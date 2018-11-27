package ch.hsr.dsa.application;

import ch.hsr.dsa.mapping.group.GroupRepository;
import ch.hsr.dsa.mapping.message.MessageRepository;
import ch.hsr.dsa.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class MessageServiceConfiguration {

    @Bean
    public MessageService messageService(MessageRepository messageRepository,
                                         GroupRepository groupRepository,
                                         PeerRepository peerRepository) {
        return new MessageService(messageRepository, groupRepository, peerRepository);
    }
}
