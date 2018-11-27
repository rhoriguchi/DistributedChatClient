package ch.hsr.dsa.event.dbchanged;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DbGroupSavedEvent extends ApplicationEvent {

    private final Long id;

    public DbGroupSavedEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
