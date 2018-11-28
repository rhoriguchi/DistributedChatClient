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
                //TODO check if this gets thrown up or caught be exceptionCaught
                if (futureDirect.isFailed()) {
                    throw new MessageHandlerException("Sending message failed");
                }
            }

            @Override
            public void exceptionCaught(Throwable throwable) {
                LOGGER.error(throwable.getMessage(), throwable);
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
                } else {
                    throw new IllegalArgumentException(
                        "Message not is instance of TomP2PMessage");
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
}
