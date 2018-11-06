package ch.hsr.event.messagereceived;

import org.springframework.context.ApplicationEvent;

public class MessageReceivedEvent extends ApplicationEvent {

    public MessageReceivedEvent(Object source) {
        super(source);
    }

}
