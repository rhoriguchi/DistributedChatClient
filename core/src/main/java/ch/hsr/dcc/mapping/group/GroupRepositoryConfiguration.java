package ch.hsr.dcc.mapping.group;

import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupRepositoryConfiguration {

    @Bean
    public GroupRepository groupRepository(DbGateway dbGateway, PeerRepository peerRepository) {
        return new GroupMapper(dbGateway, peerRepository);
    }
}
