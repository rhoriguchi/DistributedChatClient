package ch.hsr.dcc.mapping.friend;

import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendRepositoryConfiguration {

    @Bean
    public FriendRepository friendRepository(KeyStoreRepository keyStoreRepository,
                                             DbGateway dbGateway,
                                             TomP2P tomP2P,
                                             PeerRepository peerRepository) {
        return new FriendMapper(keyStoreRepository, dbGateway, tomP2P, peerRepository);
    }
}
