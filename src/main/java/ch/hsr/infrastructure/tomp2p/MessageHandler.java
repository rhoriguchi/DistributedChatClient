package ch.hsr.infrastructure.tomp2p;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import net.tomp2p.dht.FutureSend;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import java.util.LinkedList;
import java.util.Queue;

public class MessageHandler {

    private final MessageReceivedEventPublisher messageReceivedEventPublisher;
    private final PeerHolder peerHolder;

    // TODO not thread safe
    private Queue<TomP2PMessage> receivedMessagesQueue = new LinkedList();
    // TODO not thread safe
    private Queue<TomP2PGroupMessage> receivedGroupMessagesQueue = new LinkedList();

    public MessageHandler(MessageReceivedEventPublisher messageReceivedEventPublisher, PeerHolder peerHolder) {
        this.messageReceivedEventPublisher = messageReceivedEventPublisher;
        this.peerHolder = peerHolder;
    }

    public TomP2PMessage getOldestReceivedTomP2PMessage() {
        return receivedMessagesQueue.poll();
    }

    public void send(TomP2PMessage tomP2PMessage) {
        // TODO check if peer initialized

        FutureSend futureSend = peerHolder.getPeerDHT()
            .send(Number160.createHash(tomP2PMessage.getToUsername()))
            .object(tomP2PMessage)
            .start();

        futureSend.awaitUninterruptibly();
        if (futureSend.isFailed()) {
            // TODO wrong exception
            throw new IllegalArgumentException("Message could not be sent");
        }
    }

    public void initMessageReceivedEventPublisher() {
        peerHolder.getPeer().objectDataReply(new ObjectDataReply() {
            @Override
            public Object reply(PeerAddress sender, Object request) {
                if (request instanceof TomP2PMessage) {
                    TomP2PMessage tomP2PMessage = (TomP2PMessage) request;

                    // TODO to this in service
                    tomP2PMessage.setReceived(true);

                    receivedMessagesQueue.add(tomP2PMessage);
                    messageReceivedEventPublisher.messageReceived();

                    return tomP2PMessage;
                } else if (request instanceof TomP2PGroupMessage) {
                    TomP2PGroupMessage tomP2PGroupMessage = (TomP2PGroupMessage) request;

                    // TODO needs some logic go set receive and send to other peer, to this in service

                    receivedGroupMessagesQueue.add(tomP2PGroupMessage);
                    messageReceivedEventPublisher.groupMessageReceived();

                    return tomP2PGroupMessage;
                } else {
                    // TODO bad solution
                    return "ERROR";
                }
            }
        });
    }

    public TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage() {
        return receivedGroupMessagesQueue.poll();
    }
}
