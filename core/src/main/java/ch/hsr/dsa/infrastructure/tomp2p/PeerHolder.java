package ch.hsr.dsa.infrastructure.tomp2p;

import ch.hsr.dsa.infrastructure.exception.BootstrapException;
import ch.hsr.dsa.infrastructure.exception.PeerHolderException;
import ch.hsr.dsa.infrastructure.exception.PeerInitializedException;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDone;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.Inet4Address;

public class PeerHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerHolder.class);

    private final int port;

    private PeerDHT peerDHT;

    private String username;
    private String publicKey;

    public PeerHolder(int port) {
        this.port = port;
    }

    public void initPeerHolder(String username, String publicKey) {
        initPeerHolder(null, username, publicKey);
    }

    public void initPeerHolder(Inet4Address bootstrapInet4Address, String username, String publicKey) {
        if (isNotInitialized()) {
            try {
                Peer peer = initPeer(username);
                this.username = username;
                this.publicKey = publicKey;

                if (bootstrapInet4Address != null) {
                    bootstrapPeer(peer, bootstrapInet4Address);
                }

                peerDHT = new PeerBuilderDHT(peer).start();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException("PeerHolder could not be initialized");
            }
        } else {
            throw new PeerInitializedException("Peer is already initialized");
        }
    }

    private Boolean isNotInitialized() {
        return peerDHT == null;
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
            throw new BootstrapException("Peer could not be bootstrapped");
        }
    }

    public PeerObject getSelf() {
        return new PeerObject(
            username,
            publicKey,
            peerDHT.peerAddress().inetAddress().getHostAddress(),
            port,
            port
        );
    }

    public void shutdown() {
        checkInitialized();

        FutureDone<Void> futureDone = getPeer().announceShutdown()
            .start();

        futureDone.awaitUninterruptibly();

        if (futureDone.isSuccess()) {
            peerDHT = null;
            username = null;
            publicKey = null;
        } else {
            throw new PeerHolderException("Peer could not be shutdown");
        }
    }

    private void checkInitialized() {
        if (isNotInitialized()) {
            throw new PeerInitializedException("Peer is not initialized");
        }
    }

    public Peer getPeer() {
        checkInitialized();
        return peerDHT.peer();
    }

    public PeerDHT getPeerDHT() {
        checkInitialized();
        return peerDHT;
    }
}
