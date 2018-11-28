package ch.hsr.dcc.event.dbchanged;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DbMessageSavedEvent extends ApplicationEvent {

    private final Long id;

    public DbMessageSavedEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
