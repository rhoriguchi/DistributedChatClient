package ch.hsr.dcc.infrastructure.tomp2p.message;

import ch.hsr.dcc.infrastructure.exception.MessageHandlerException;
import ch.hsr.dcc.infrastructure.tomp2p.PeerHolder;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final PeerHolder peerHolder;
    private final MessageQueHolder messageQueHolder;

    public MessageHandler(PeerHolder peerHolder,
                          MessageQueHolder messageQueHolder) {
        this.peerHolder = peerHolder;
        this.messageQueHolder = messageQueHolder;
    }

    public void send(TomP2PFriendRequest tomP2PFriendRequest, TomP2PPeerAddress tomP2PPeerAddress) {
        sendObject(tomP2PFriendRequest, tomP2PPeerAddress);
    }

    private void sendObject(Object object, TomP2PPeerAddress tomP2PPeerAddress) {
        FutureDirect futureDirect = peerHolder.getPeer()
            .sendDirect(tomP2PPeerAddressToPeerAddress(tomP2PPeerAddress))
            .object(object)
            .start();

        futureDirect.addListener(new BaseFutureListener<FutureDirect>() {
            @Override
            public void operationComplete(FutureDirect futureDirect) {
                if (futureDirect.isFailed()) {
                    throw new MessageHandlerException("Sending message failed");
                }
            }

            @Override
            public void exceptionCaught(Throwable throwable) {
                LOGGER.error(throwable.getMessage(), throwable);
                throw new MessageHandlerException(throwable.getMessage());
            }
        });
    }

    private PeerAddress tomP2PPeerAddressToPeerAddress(TomP2PPeerAddress tomP2PPeerAddress) {
        try {
            return new PeerAddress(
                Number160.createHash(tomP2PPeerAddress.getUsername()),
                InetAddress.getByName(tomP2PPeerAddress.getIpAddress()),
                tomP2PPeerAddress.getTcpPort(),
                tomP2PPeerAddress.getUdpPort()
            );
        } catch (UnknownHostException e) {
            throw new MessageHandlerException(String.format("Could not resolve ipAddress %s",
                tomP2PPeerAddress.getIpAddress()));
        }
    }

    public void send(TomP2PMessage tomP2PMessage, TomP2PPeerAddress tomP2PPeerAddress) {
        sendObject(tomP2PMessage, tomP2PPeerAddress);
    }

    public void initMessageReceivedEventPublisher() {
        peerHolder.getPeer().objectDataReply(new ObjectDataReply() {
            @Override
            public TomP2PMessage reply(PeerAddress sender, Object request) {
                if (request instanceof TomP2PMessage) {
                    messageQueHolder.addMessageToQueue((TomP2PMessage) request);
                } else if (request instanceof TomP2PFriendRequest) {
                    messageQueHolder.addFriendRequestToQueue((TomP2PFriendRequest) request);
                } else if (request instanceof TomP2PGroupAdd) {
                    messageQueHolder.addTomP2PGroupAddToQueue((TomP2PGroupAdd) request);
                } else {
                    throw new IllegalArgumentException(
                        "Message is not valid class");
                }

                return null;
            }
        });
    }

    public TomP2PGroupMessage getOldestReceivedGroupMessage() {
        return messageQueHolder.getOldestReceivedGroupMessage();
    }

    public TomP2PFriendRequest getOldestReceivedFriendRequest() {
        return messageQueHolder.getOldestReceivedFriendRequest();
    }

    public TomP2PMessage getOldestReceivedMessage() {
        return messageQueHolder.getOldestReceivedMessage();
    }

    public void sendGroupAdd(TomP2PGroupAdd groupToTomP2PGroupAdd, TomP2PPeerAddress tomP2PPeerAddress) {
        sendObject(groupToTomP2PGroupAdd, tomP2PPeerAddress);
    }

    public TomP2PGroupAdd getOldestReceivedGroupAdd() {
        return messageQueHolder.getOldestReceivedGroupAdd();
    }
}