package ch.hsr.infrastructure.tomp2p;

import ch.hsr.event.messagereceived.MessageReceivedEventPublisher;
import ch.hsr.infrastructure.tomp2p.message.TomP2PDefaultMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PDefaultMessageState;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.dht.FutureSend;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.Queue;

public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

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
        if (peerHolder.isInitialized()) {
            FutureSend futureSend = peerHolder.getPeerDHT()
                .send(Number160.createHash(tomP2PMessage.getToUsername()))
                .object(tomP2PMessage)
                .start();

            futureSend.awaitUninterruptibly();
            if (futureSend.isFailed()) {
                // TODO wrong exception
                throw new IllegalArgumentException("Message could not be sent");
            }
        } else {
            // TODO wrong exception
            throw new IllegalArgumentException("Peer needs to be initialized");
        }
    }

    public void initMessageReceivedEventPublisher() {
        peerHolder.getPeer().objectDataReply(new ObjectDataReply() {
            @Override
            public TomP2PDefaultMessage reply(PeerAddress sender, Object request) {
                // TODO a bit redundant code
                // TODO find better solution for this logic
                if (request instanceof TomP2PDefaultMessage) {
                    TomP2PDefaultMessage tomP2PDefaultMessage = (TomP2PDefaultMessage) request;

                    if (request instanceof TomP2PMessage) {
                        TomP2PMessage tomP2PMessage = (TomP2PMessage) request;

                        receivedMessagesQueue.add(tomP2PMessage);
                        messageReceivedEventPublisher.messageReceived();

                        if (tomP2PMessage.getState() == TomP2PDefaultMessageState.SENT) {
                            tomP2PMessage.setState(TomP2PDefaultMessageState.RECEIVED);
                            return tomP2PMessage;
                        }
                    } else if (request instanceof TomP2PGroupMessage) {
                        TomP2PGroupMessage tomP2PGroupMessage = (TomP2PGroupMessage) request;

                        receivedGroupMessagesQueue.add(tomP2PGroupMessage);
                        messageReceivedEventPublisher.groupMessageReceived();

                        if (tomP2PGroupMessage.getState() == TomP2PDefaultMessageState.SENT) {
                            tomP2PGroupMessage.setState(TomP2PDefaultMessageState.RECEIVED);
                            return tomP2PGroupMessage;
                        }
                    } else {
                        if (tomP2PDefaultMessage.getState() == TomP2PDefaultMessageState.ERROR) {
                            LOGGER.error(tomP2PDefaultMessage.toString());
                        } else {
                            tomP2PDefaultMessage.setState(TomP2PDefaultMessageState.ERROR);
                            return tomP2PDefaultMessage;
                        }
                    }
                } else {
                    LOGGER.error("Message was received that is not instance of TomP2PDefaultMessage");
                }
            }
        });
    }

    public TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage() {
        return receivedGroupMessagesQueue.poll();
    }
}
