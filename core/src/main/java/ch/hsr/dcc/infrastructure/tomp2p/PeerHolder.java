package ch.hsr.dcc.infrastructure.tomp2p;

import ch.hsr.dcc.infrastructure.exception.BootstrapException;
import ch.hsr.dcc.infrastructure.exception.PeerHolderException;
import ch.hsr.dcc.infrastructure.exception.PeerInitializedException;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDone;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PeerHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerHolder.class);

    private final int port;

    private PeerDHT peerDHT;

    private String username;

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
                this.username = username;

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

    public TomP2PPeerObject getSelf() {
        return new TomP2PPeerObject(
            username,
            getLocalHostAddress(),
            port,
            port
        );
    }

    private String getLocalHostAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            LOGGER.error(e.getMessage(), e);
            return peerDHT.peerAddress().inetAddress().getHostAddress();
        }
    }

    public void shutdown() {
        if (!isNotInitialized()) {
            FutureDone<Void> futureDone = getPeer().announceShutdown()
                .start();

            futureDone.awaitUninterruptibly();

            BaseFuture baseFuture = peerDHT.shutdown();
            baseFuture.awaitUninterruptibly();

            if (baseFuture.isSuccess()) {
                peerDHT = null;
                username = null;
            } else {
                throw new PeerHolderException("Peer could not be shutdown");
            }
        }
    }

    public Peer getPeer() {
        checkInitialized();
        return peerDHT.peer();
    }

    private void checkInitialized() {
        if (isNotInitialized()) {
            throw new PeerInitializedException("Peer is not initialized");
        }
    }

    public PeerDHT getPeerDHT() {
        checkInitialized();
        return peerDHT;
    }
}
