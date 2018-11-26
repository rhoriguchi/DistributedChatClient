package ch.hsr.dsa.mapping.message;

import ch.hsr.dsa.infrastructure.db.DbGateway;
import ch.hsr.dsa.infrastructure.tomp2p.TomP2P;
import ch.hsr.dsa.mapping.group.GroupRepository;
import ch.hsr.dsa.mapping.keystore.KeyStoreRepository;
import ch.hsr.dsa.mapping.peer.PeerRepository;
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
