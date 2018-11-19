package ch.hsr.infrastructure.tomp2p.message;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import java.util.LinkedList;
import java.util.Queue;


public class TomP2PMessageQueHolder {

    private final MessageReceivedEventPublisher messageReceivedEventPublisher;

    // TODO not thread safe
    private Queue<TomP2PMessage> receivedMessagesQueue = new LinkedList();
    // TODO not thread safe
    private Queue<TomP2PGroupMessage> receivedGroupMessagesQueue = new LinkedList();

    public TomP2PMessageQueHolder(MessageReceivedEventPublisher messageReceivedEventPublisher) {
        this.messageReceivedEventPublisher = messageReceivedEventPublisher;
    }

    public void addMessageToQue(DefaultTomP2PMessage defaultTomP2PMessage) {
        if (defaultTomP2PMessage instanceof TomP2PGroupMessage) {
            receivedGroupMessagesQueue.add((TomP2PGroupMessage) defaultTomP2PMessage);
            messageReceivedEventPublisher.groupMessageReceived();
        } else if (defaultTomP2PMessage instanceof TomP2PMessage) {
            receivedMessagesQueue.add((TomP2PMessage) defaultTomP2PMessage);
            messageReceivedEventPublisher.messageReceived();
        } else {
            // TODO wrong exception
            throw new IllegalArgumentException("Object is not an instance of a message");
        }
    }

    public TomP2PMessage getOldestReceivedMessage() {
        return receivedMessagesQueue.poll();
    }

    public TomP2PGroupMessage getOldestReceivedGroupMessage() {
        return receivedGroupMessagesQueue.poll();
    }
}