package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.mapping.group.GroupMapper;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.peer.PeerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupRepositoryConfiguration {

    @Bean
    public GroupRepository groupRepository(DbGateway dbGateway, PeerMapper peerMapper) {
        return new GroupMapper(dbGateway, peerMapper);
    }
}
