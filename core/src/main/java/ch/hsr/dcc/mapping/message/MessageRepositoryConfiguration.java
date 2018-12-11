package ch.hsr.dcc.mapping.message;

import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.notary.NotaryRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRepositoryConfiguration {

    @Bean
    public MessageRepository messageRepository(DbGateway dbGateway,
                                               TomP2P tomP2P,
                                               PeerRepository peerRepository,
                                               GroupRepository groupRepository,
                                               NotaryRepository notaryRepository) {
        return new MessageMapper(dbGateway, tomP2P, peerRepository, groupRepository, notaryRepository);
    }
}
