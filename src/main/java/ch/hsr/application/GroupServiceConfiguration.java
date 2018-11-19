package ch.hsr.application.configuration;

import ch.hsr.application.GroupService;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupServiceConfiguration {

    @Bean
    public GroupService groupService(GroupRepository groupRepository, PeerRepository peerRepository) {
        return new GroupService(groupRepository, peerRepository);
    }
}
