package ch.hsr.dcc.scheduler;

import ch.hsr.dcc.mapping.group.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class GroupScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupScheduler.class);

    private final GroupRepository groupRepository;

    public GroupScheduler(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Scheduled (fixedDelay = 300_000)
    public synchronized void synchronizeGroups() {
        try {
            groupRepository.synchronizeGroups();
        } catch (NullPointerException ignored) {
            //TODO good solution
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
