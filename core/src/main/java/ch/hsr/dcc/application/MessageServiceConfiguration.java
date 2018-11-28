package ch.hsr.dcc.application;

import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.message.MessageRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
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
