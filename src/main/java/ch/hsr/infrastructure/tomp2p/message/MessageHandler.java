package ch.hsr.infrastructure.tomp2p.message;

import ch.hsr.infrastructure.exception.MessageHandlerException;
import ch.hsr.infrastructure.tomp2p.PeerHolder;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.UnknownHostException;

public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final PeerHolder peerHolder;
    private final TomP2PMessageQueHolder tomP2PMessageQueHolder;

    public MessageHandler(PeerHolder peerHolder,
                          TomP2PMessageQueHolder tomP2PMessageQueHolder) {
        this.peerHolder = peerHolder;
        this.tomP2PMessageQueHolder = tomP2PMessageQueHolder;
    }

    public void send(DefaultTomP2PMessage defaultTomP2PMessage) {
        try {
            FutureDirect futureDirect = peerHolder.getPeer()
                .sendDirect(getPeerAddress(defaultTomP2PMessage.getToUsername()))
                .object(defaultTomP2PMessage)
                .start();

            futureDirect.addListener(new BaseFutureListener<FutureDirect>() {
                @Override
                public void operationComplete(FutureDirect futureDirect) {
                    if (futureDirect.isFailed()) {
                        throw new MessageHandlerException(String.format(
                            "Message '%s' could not be sent, peer is not online",
                            defaultTomP2PMessage.toString()));
                    }
                }

                @Override
                public void exceptionCaught(Throwable throwable) {
                    // TODO handle exception
//                        defaultTomP2PMessage.setStates(TomP2PMessageState.ERROR);
//                        tomP2PMessageQueHolder.addMessageToQue(defaultTomP2PMessage);
//                        LOGGER.error(throwable.getMessage(), throwable);
                }
            });
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
            // TODO throw some kind of exception
        }
    }

    private PeerAddress getPeerAddress(String username) throws UnknownHostException {
        // TODO does not work
        Peer peer = null;

        return new PeerAddress(
            Number160.createHash(username),
            peer.peerAddress().inetAddress(),
            peer.peerAddress().tcpPort(),
            peer.peerAddress().udpPort()
        );
    }

    public void initMessageReceivedEventPublisher() {
        peerHolder.getPeer().objectDataReply(new ObjectDataReply() {
            @Override
            public DefaultTomP2PMessage reply(PeerAddress sender, Object request) {
                if (request instanceof DefaultTomP2PMessage) {
                    DefaultTomP2PMessage defaultTomP2PMessage = (DefaultTomP2PMessage) request;

                    tomP2PMessageQueHolder.addMessageToQue(defaultTomP2PMessage);
                } else {
                    throw new IllegalArgumentException(
                        "Message was states that is not instance of TomP2PMessage");
                }

                return null;
            }
        });
    }

    public TomP2PGroupMessage getOldestReceivedGroupMessage() {
        return tomP2PMessageQueHolder.getOldestReceivedGroupMessage();
    }

    public TomP2PMessage getOldestReceivedMessage() {
        return tomP2PMessageQueHolder.getOldestReceivedMessage();
    }
}
