package ch.hsr.dcc.infrastructure.tomp2p.message;

import ch.hsr.dcc.event.messagereceived.MessageReceivedEventPublisher;
import ch.hsr.dcc.infrastructure.exception.MessageQueException;
import java.util.LinkedList;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;


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
        checkArgument(receivedMessagesQueue.isEmpty(), "received messages Queue is empty");
        return receivedMessagesQueue.poll();
    }

    public synchronized TomP2PGroupMessage getOldestReceivedGroupMessage() {
        checkArgument(receivedMessagesQueue.isEmpty(), "Received group messages Queue is empty");
        return receivedGroupMessagesQueue.poll();
    }

    public synchronized TomP2PFriendRequest getOldestReceivedFriendRequest() {
        checkArgument(receivedMessagesQueue.isEmpty(), "Friend request Queue is empty");
        return receivedFriendRequestsQueue.poll();
    }

    public synchronized void addFriendRequestToQueue(TomP2PFriendRequest tomP2PFriendRequest) {
        receivedFriendRequestsQueue.add(tomP2PFriendRequest);
        messageReceivedEventPublisher.friendRequestReceived();
    }
}
