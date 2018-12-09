package ch.hsr.dcc.event.messagereceived;

import org.springframework.context.ApplicationEvent;

public class FriendRequestReceivedEvent extends ApplicationEvent {

    public FriendRequestReceivedEvent(Object source) {
        super(source);
    }
}
