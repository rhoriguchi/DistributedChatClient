package ch.hsr.dsa.event.messagereceived;

import org.springframework.context.ApplicationEvent;

public class GroupMessageReceivedEvent extends ApplicationEvent {

    public GroupMessageReceivedEvent(Object source) {
        super(source);
    }

}
