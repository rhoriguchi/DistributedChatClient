package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.keystore.KeyStoreRepository;
import ch.hsr.mapping.message.MessageMapper;
import ch.hsr.mapping.message.MessageRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRepositoryConfiguration {

    @Bean
    public MessageRepository messageRepository(DbGateway dbGateway,
                                               TomP2P tomP2P,
                                               PeerRepository peerRepository,
                                               GroupRepository groupRepository,
                                               KeyStoreRepository keyStoreRepository) {
        return new MessageMapper(dbGateway, tomP2P, peerRepository, groupRepository, keyStoreRepository);
    }
}
