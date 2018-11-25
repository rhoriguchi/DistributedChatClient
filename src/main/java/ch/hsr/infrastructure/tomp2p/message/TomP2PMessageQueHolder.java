package ch.hsr.infrastructure.tomp2p.message;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import ch.hsr.infrastructure.exception.MessageQueException;
import java.util.LinkedList;
import java.util.Queue;


//TODO check if que empty and throw exception
public class TomP2PMessageQueHolder {

    private final MessageReceivedEventPublisher messageReceivedEventPublisher;

    private volatile Queue<TomP2PMessage> receivedMessagesQueue = new LinkedList<>();
    private volatile Queue<TomP2PGroupMessage> receivedGroupMessagesQueue = new LinkedList<>();
    private volatile Queue<TomP2PFriendRequest> receivedFriendRequestsQueue = new LinkedList<>();

    public TomP2PMessageQueHolder(MessageReceivedEventPublisher messageReceivedEventPublisher) {
        this.messageReceivedEventPublisher = messageReceivedEventPublisher;
    }

    public synchronized void addMessageToQueue(TomP2PMessage tomP2PMessage) {
        if (tomP2PMessage instanceof TomP2PGroupMessage) {
            receivedGroupMessagesQueue.add((TomP2PGroupMessage) tomP2PMessage);
            messageReceivedEventPublisher.groupMessageReceived();
            //TODO intellij complains
        } else if (tomP2PMessage instanceof TomP2PMessage) {
            receivedMessagesQueue.add(tomP2PMessage);
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

    public synchronized TomP2PFriendRequest getOldestReceivedFriendRequest() {
        return receivedFriendRequestsQueue.poll();
    }

    public synchronized void addFriendRequestToQueue(TomP2PFriendRequest tomP2PFriendRequest) {
        receivedFriendRequestsQueue.add(tomP2PFriendRequest);
        messageReceivedEventPublisher.friendRequestReceived();
    }
}
