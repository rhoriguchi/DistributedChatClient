package ch.hsr.infrastructure.tomp2p.message;

import ch.hsr.infrastructure.tomp2p.PeerHolder;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
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

    public void send(TomP2PMessage tomP2PMessage) {
        // TODO for all failed cases the state has to get set ERROR in db
        if (peerHolder.isInitialized()) {
            FutureDirect futureDirect = null;
            try {
                futureDirect = peerHolder.getPeer()
                    .sendDirect(getPeerAddress(tomP2PMessage.getToUsername()))
                    .object(tomP2PMessage)
                    .start();

                futureDirect.addListener(new BaseFutureListener<FutureDirect>() {
                    @Override
                    public void operationComplete(FutureDirect futureDirect) {
                        if (futureDirect.isFailed()) {
                            throw new IllegalArgumentException(String.format(
                                "Message \"%s\" could not be sent, peer is not online",
                                tomP2PMessage.toString()));
                        }
                    }

                    @Override
                    public void exceptionCaught(Throwable throwable) {
                        tomP2PMessage.setState(TomP2PMessageState.ERROR);
                        tomP2PMessageQueHolder.addMessageToQue(tomP2PMessage);
                        LOGGER.error(throwable.getMessage(), throwable);
                    }
                });
            } catch (UnknownHostException e) {
                // TODO do something with this
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            // TODO wrong exception
            throw new IllegalArgumentException("Peer needs to be initialized");
        }
    }

    private PeerAddress getPeerAddress(String username) throws UnknownHostException {
        peerHolder.getPeer().discover()

        InetAddress ipAddress = Inet4Address.getByName(contactState.getIp());
        return new PeerAddress(Number160.createHash(contact.getName()),
            ipAddress,
            contactState.getPort(),
            contactState.getPort());
    }

    public void initMessageReceivedEventPublisher() {
        peerHolder.getPeer().objectDataReply(new ObjectDataReply() {
            @Override
            public TomP2PMessage reply(PeerAddress sender, Object request) {
                if (request instanceof TomP2PMessage) {
                    TomP2PMessage tomP2PMessage = (TomP2PMessage) request;

                    if (request instanceof TomP2PMessage) {
                        TomP2PMessage tomP2PMessage = (TomP2PMessage) request;

                        tomP2PMessageQueHolder.addMessageToQue(tomP2PMessage);

                        if (tomP2PMessage.getState() == TomP2PMessageState.SENT) {
                            tomP2PMessage.setState(TomP2PMessageState.RECEIVED);
                            return tomP2PMessage;
                        }
                    } else if (request instanceof TomP2PGroupMessage) {
                        TomP2PGroupMessage tomP2PGroupMessage = (TomP2PGroupMessage) request;

                        tomP2PMessageQueHolder.addMessageToQue(tomP2PGroupMessage);

                        if (tomP2PGroupMessage.getState() == TomP2PMessageState.SENT) {
                            tomP2PGroupMessage.setState(TomP2PMessageState.RECEIVED);
                            return tomP2PGroupMessage;
                        }
                    } else {
                        if (tomP2PMessage.getState() == TomP2PMessageState.ERROR) {
                            LOGGER.error(tomP2PMessage.toString());
                        } else {
                            tomP2PMessage.setState(TomP2PMessageState.ERROR);
                            return tomP2PMessage;
                        }
                    }
                } else {
                    throw new IllegalArgumentException(
                        "Message was states that is not instance of TomP2PMessage");
                }
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
