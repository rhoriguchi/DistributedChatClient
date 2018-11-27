package ch.hsr.dsa.application;

import ch.hsr.dsa.mapping.group.GroupRepository;
import ch.hsr.dsa.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class GroupServiceConfiguration {

    @Bean
    public GroupService groupService(GroupRepository groupRepository, PeerRepository peerRepository) {
        return new GroupService(groupRepository, peerRepository);
    }
}
