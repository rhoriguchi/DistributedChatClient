package ch.hsr.infrastructure.tomp2p;

import ch.hsr.event.message.MessageReceivedEventPublisher;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.FutureSend;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.LinkedList;
import java.util.Queue;

public class TomP2PImplementation implements TomP2P {

    private static final Logger LOGGER = LoggerFactory.getLogger(TomP2PImplementation.class);

    private final MessageReceivedEventPublisher messageReceivedEventPublisher;
    private final int port;

    private PeerObject self;

    // TODO not thread safe
    private Queue<TomP2PMessage> receivedMessagesQueue = new LinkedList();

    public TomP2PImplementation(MessageReceivedEventPublisher messageReceivedEventPublisher, int port) {
        this.messageReceivedEventPublisher = messageReceivedEventPublisher;
        this.port = port;
    }

    @PostConstruct
    public void initMessageReceivedEventPublisher() {
        self.getPeer().objectDataReply(new ObjectDataReply() {
            @Override
            public Object reply(PeerAddress sender, Object request) {
                if (request instanceof TomP2PMessage) {
                    TomP2PMessage tomP2PMessage = (TomP2PMessage) request;
                    tomP2PMessage.setReceived(true);

                    receivedMessagesQueue.add(tomP2PMessage);
                    messageReceivedEventPublisher.messageReceived();

                    return tomP2PMessage;
                } else {
                    // TODO bad solution
                    return "ERROR";
                }
            }
        });
    }

    @Override
    public boolean login(String username) {
        return login(null, username);
    }

    @Override
    public boolean login(Inet4Address bootstrapInet4Address, String username) {
        if (self == null) {
            try {
                Peer peer = initPeer(username);

                if (bootstrapInet4Address != null) {
                    bootstrapPeer(peer, bootstrapInet4Address);
                }

                self = new PeerObject(new PeerBuilderDHT(peer).start());

                addUsernameToDHT(username);
            } catch (IOException e) {
                // TODO maybe handle this exception
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            // TODO is initialized message or exception?
        }

        return true;
    }

    private Peer initPeer(String username) throws IOException {
        return new PeerBuilder(Number160.createHash(username))
            .ports(port)
            .start();
    }

    private void bootstrapPeer(Peer peer, Inet4Address inet4Address) {
        FutureBootstrap futureBootstrap = peer.bootstrap()
            .inetAddress(inet4Address)
            .ports(port)
            .start();

        futureBootstrap.awaitUninterruptibly();
        if (futureBootstrap.isFailed()) {
            // TODO wrong exception
            throw new IllegalArgumentException("Peer could not be bootstrapped");
        }
    }

    private void addUsernameToDHT(String username) {
        addStringToDHT(Number160.createHash(username), username);
    }

    private void addStringToDHT(Number160 key, String value) {
        try {
            addDataToDHT(key, new Data(value));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void addDataToDHT(Number160 key, Data data) {
        checkSelfInitialized();

        if (!key.toString().isEmpty()) {
            FuturePut futurePut = self.getPeerDHT()
                .put(key)
                .data(data)
                .start();

            futurePut.awaitUninterruptibly();
            if (futurePut.isFailed()) {
                // TODO wrong exception
                throw new IllegalArgumentException("Username could not be added to distributed hash table");
            }
        } else {
            throw new IllegalArgumentException("Key can't be empty");
        }
    }

    private void checkSelfInitialized() {
        if (self == null) {
            // TODO wrong exception
            throw new IllegalArgumentException("Self needs to be initialized");
        }
    }

    @Override
    public void logout() {
        checkSelfInitialized();

        self.getPeer().announceShutdown();
        self.getPeer().shutdown();

        self = null;
    }

    @Override
    public String getUserName(Number160 peerId) {
        FutureGet futureGet = self.getPeerDHT().get(peerId).start();
        return futureGet.data().toString();
    }

    @Override
    public PeerObject getSelf() {
        return self;
    }

    @Override
    public TomP2PMessage getOldestReceivedTomP2PMessage() {
        return receivedMessagesQueue.poll();
    }

    @Override
    public String getPeerId(String username) {
        return Number160.createHash(username).toString();
    }

    @Override
    public void sendMessage(TomP2PMessage tomP2PMessage) {
        FutureSend futureSend = self.getPeerDHT()
            .send(Number160.createHash(tomP2PMessage.getToUsername()))
            .object(tomP2PMessage)
            .start();
        futureSend.awaitUninterruptibly();

        if (futureSend.isFailed()) {
            // TODO wrong exception
            throw new IllegalArgumentException("Message could not be sent");
        }
    }
}
