package ch.hsr.dcc.event.messagereceived;

import org.springframework.context.ApplicationEventPublisher;

public class MessageReceivedEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public MessageReceivedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void messageReceived() {
        applicationEventPublisher.publishEvent(new MessageReceivedEvent(this));
    }

    public void groupMessageReceived() {
        applicationEventPublisher.publishEvent(new GroupMessageReceivedEvent(this));
    }

    public void friendRequestReceived() {
        applicationEventPublisher.publishEvent(new FriendRequestReceivedEvent(this));
    }

    public void groupAddReceived() {
        applicationEventPublisher.publishEvent(new GroupAddReceivedEvent(this));
    }
}
