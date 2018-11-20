package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.exception.BootstrapException;
import ch.hsr.infrastructure.exception.PeerHolderException;
import ch.hsr.infrastructure.exception.PeerInitializedException;
import lombok.Getter;
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

    public PeerHolder(int port) {
        this.port = port;
    }

    public void initPeerHolder(String username) {
        initPeerHolder(null, username);
    }

    public void initPeerHolder(Inet4Address bootstrapInet4Address, String username) {
        if (isNotInitialized()) {
            try {
                Peer peer = initPeer(username);

                if (bootstrapInet4Address != null) {
                    bootstrapPeer(peer, bootstrapInet4Address);
                }

                peerDHT = new PeerBuilderDHT(peer).start();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException("PeerHolder could not be initilized");
            }
        } else {
            throw new PeerInitializedException("Peer is already initialized");
        }
    }

    private Boolean isNotInitialized() {
        return peerDHT == null;
    }

    private void checkInitialized() {
        if (isNotInitialized()) {
            throw new PeerInitializedException("Peer is not initialized");
        }
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

    public void shutdown() {
        checkInitialized();

        FutureDone<Void> futureDone = getPeer().announceShutdown()
            .start();

        futureDone.awaitUninterruptibly();

        if (futureDone.isSuccess()) {
            peerDHT = null;
        } else {
            throw new PeerHolderException("Peer could not be shutdown");
        }
    }

    public PeerDHT getPeerDHT() {
        checkInitialized();
        return peerDHT;
    }

    public Peer getPeer() {
        checkInitialized();
        return peerDHT.peer();
    }
}
