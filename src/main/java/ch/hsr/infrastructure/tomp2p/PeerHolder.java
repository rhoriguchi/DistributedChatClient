package ch.hsr.infrastructure.tomp2p;

import lombok.Getter;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
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

    @Getter
    private PeerObject peerObject;

    public PeerHolder(int port) {
        this.port = port;
    }

    public void initPeerHolder(String username) {
        initPeerHolder(null, username);
    }

    public void initPeerHolder(Inet4Address bootstrapInet4Address, String username) {
        if (!isInitialized()) {
            try {
                Peer peer = initPeer(username);

                if (bootstrapInet4Address != null) {
                    bootstrapPeer(peer, bootstrapInet4Address);
                }

                peerObject = new PeerObject(new PeerBuilderDHT(peer).start());
            } catch (IOException e) {
                // TODO maybe handle this exception or bubble it up
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            // TODO wrong exception
            throw new IllegalArgumentException("Peer is already initialized");
        }
    }

    private Boolean isInitialized() {
        return peerObject != null;
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

    public PeerDHT getPeerDHT() {
        if (isInitialized()) {
            return peerObject.getPeerDHT();
        } else {
            return null;
        }
    }

    public void shutdown() {
        if (isInitialized()) {
            getPeer().announceShutdown();
            // TODO maybe wait a couple seconds
            getPeer().shutdown();

            peerObject = null;
        } else {
            // TODO wrong exception
            throw new IllegalArgumentException("Peer needs to be initialized to shut down");
        }
    }

    public Peer getPeer() {
        if (isInitialized()) {
            return peerObject.getPeer();
        } else {
            return null;
        }
    }
}
