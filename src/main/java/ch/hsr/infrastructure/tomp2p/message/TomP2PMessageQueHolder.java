package ch.hsr.infrastructure.tomp2p.message;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import ch.hsr.infrastructure.exception.MessageQueException;
import java.util.LinkedList;
import java.util.Queue;


public class TomP2PMessageQueHolder {

    private final MessageReceivedEventPublisher messageReceivedEventPublisher;

    private volatile Queue<TomP2PMessage> receivedMessagesQueue = new LinkedList();
    private volatile Queue<TomP2PGroupMessage> receivedGroupMessagesQueue = new LinkedList();

    public TomP2PMessageQueHolder(MessageReceivedEventPublisher messageReceivedEventPublisher) {
        this.messageReceivedEventPublisher = messageReceivedEventPublisher;
    }

    public synchronized void addMessageToQue(DefaultTomP2PMessage defaultTomP2PMessage) {
        if (defaultTomP2PMessage instanceof TomP2PGroupMessage) {
            receivedGroupMessagesQueue.add((TomP2PGroupMessage) defaultTomP2PMessage);
            messageReceivedEventPublisher.groupMessageReceived();
        } else if (defaultTomP2PMessage instanceof TomP2PMessage) {
            receivedMessagesQueue.add((TomP2PMessage) defaultTomP2PMessage);
            messageReceivedEventPublisher.messageReceived();
        } else {
            throw new MessageQueException("Object is not an instance of a message");
        }
    }

    public synchronized TomP2PMessage getOldestReceivedMessage() {
        return receivedMessagesQueue.poll();
    }

    public synchronized TomP2PGroupMessage getOldestReceivedGroupMessage() {
        return receivedGroupMessagesQueue.poll();
    }
}
