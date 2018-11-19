package ch.hsr.mapping.group;

import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupRepositoryConfiguration {

    @Bean
    public GroupRepository groupRepository(DbGateway dbGateway, PeerRepository peerRepository) {
        return new GroupMapper(dbGateway, peerRepository);
    }
}
