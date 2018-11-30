package ch.hsr.dcc.mapping.group;

import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupRepositoryConfiguration {

    @Bean
    public GroupRepository groupRepository(TomP2P tomP2P,
                                           DbGateway dbGateway,
                                           PeerRepository peerRepository,
                                           KeyStoreRepository keyStoreRepository) {
        return new GroupMapper(tomP2P, dbGateway, peerRepository, keyStoreRepository);
    }
}
