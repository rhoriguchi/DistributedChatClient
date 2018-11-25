package ch.hsr.event.messagereceived;

import org.springframework.context.ApplicationEvent;

public class FriendRequestEvent extends ApplicationEvent {

    public FriendRequestEvent(Object source) {
        super(source);
    }
}
