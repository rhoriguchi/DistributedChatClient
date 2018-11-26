package ch.hsr.dsa.mapping.friend;

import ch.hsr.dsa.infrastructure.db.DbGateway;
import ch.hsr.dsa.infrastructure.tomp2p.TomP2P;
import ch.hsr.dsa.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendRepositoryConfiguration {

    @Bean
    public FriendRepository friendRepository(DbGateway dbGateway,
                                             TomP2P tomP2P,
                                             PeerRepository peerRepository) {
        return new FriendMapper(dbGateway, tomP2P, peerRepository);
    }
}
