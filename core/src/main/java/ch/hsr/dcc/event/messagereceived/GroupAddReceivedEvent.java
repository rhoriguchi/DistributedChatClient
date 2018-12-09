package ch.hsr.dcc.event.messagereceived;

import org.springframework.context.ApplicationEvent;

public class GroupAddReceivedEvent extends ApplicationEvent {

    public GroupAddReceivedEvent(Object source) {
        super(source);
    }
}
