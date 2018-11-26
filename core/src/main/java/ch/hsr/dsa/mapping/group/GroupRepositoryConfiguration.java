package ch.hsr.dsa.mapping.group;

import ch.hsr.dsa.infrastructure.db.DbGateway;
import ch.hsr.dsa.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupRepositoryConfiguration {

    @Bean
    public GroupRepository groupRepository(DbGateway dbGateway, PeerRepository peerRepository) {
        return new GroupMapper(dbGateway, peerRepository);
    }
}
