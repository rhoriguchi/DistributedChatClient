package ch.hsr.dcc.event.dbchanged;

import org.springframework.context.ApplicationEventPublisher;

public class DbSavedEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public DbSavedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void dbFriendChanged(String username) {
        applicationEventPublisher.publishEvent(new DbFriendSavedEvent(this, username));
    }

    public void dbGroupChanged(Long id) {
        applicationEventPublisher.publishEvent(new DbGroupSavedEvent(this, id));
    }

    public void dbMessageEventChanged(Long id) {
        applicationEventPublisher.publishEvent(new DbMessageSavedEvent(this, id));
    }

    public void dbGroupMessageEventChanged(Long id) {
        applicationEventPublisher.publishEvent(new DbGroupMessageSavedEvent(this, id));
    }
}
