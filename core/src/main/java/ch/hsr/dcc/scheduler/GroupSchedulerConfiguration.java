package ch.hsr.dcc.scheduler;

import ch.hsr.dcc.mapping.group.GroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class GroupSchedulerConfiguration {

    @Bean
    public GroupScheduler groupScheduler(GroupRepository groupRepository) {
        return new GroupScheduler(groupRepository);
    }
}
