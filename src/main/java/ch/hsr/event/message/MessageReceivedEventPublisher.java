package ch.hsr.event.message;

import org.springframework.context.ApplicationEventPublisher;

public class MessageReceivedEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public MessageReceivedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void messageReceived() {
        applicationEventPublisher.publishEvent(new MessageReceivedEvent(this));
    }
}
