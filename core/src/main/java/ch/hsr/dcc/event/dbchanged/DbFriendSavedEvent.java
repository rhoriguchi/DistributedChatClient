package ch.hsr.dcc.event.dbchanged;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DbFriendSavedEvent extends ApplicationEvent {

    private final String username;

    public DbFriendSavedEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
