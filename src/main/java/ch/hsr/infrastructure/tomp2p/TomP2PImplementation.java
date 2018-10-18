package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.io.IOException;

@Repository
public class TomP2PImplementation implements TomP2P {

    private static final Logger LOGGER = LoggerFactory.getLogger(TomP2PImplementation.class);

    private final int port;

    private PeerObject self;

    public TomP2PImplementation(int port) {
        this.port = port;
    }

    @Override
    public boolean login(PeerAddress bootstrapPeerAddress, String username) {
        boolean success = false;
        Number160 usernameHash = Number160.createHash(username);

        if (self == null) {
            try {
                self = new PeerObject(
                    new PeerBuilderDHT(
                        new PeerBuilder(usernameHash).ports(port).start()
                    ).start()
                );

                success = bootstrap(bootstrapPeerAddress);
            } catch (IOException e) {
                // TODO maybe handle this exception
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            // TODO throw already connected exception
        }

        if (success) {
            // TODO try more than once when it fails
            addStringToDHT(usernameHash, username);
        }

        return success;
    }

    @Override
    public boolean addStringToDHT(Number160 key, String value) {
        try {
            return addDataToDHT(key, new Data(value));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);

            return false;
        }
    }

    private boolean addDataToDHT(Number160 key, Data data) {
        FuturePut future;

        if (!key.toString().isEmpty()) {
            future = self.getPeerDHT().put(key).data(data)
                .start();

            try {
                future.awaitListeners();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);

                return false;
            }
        } else {
            throw new IllegalArgumentException("Key can't be empty");
        }

        return future.isSuccess();
    }

    private boolean bootstrap(PeerAddress bootstrapPeerAddress) {
        FutureBootstrap future;

        if (!bootstrapPeerAddress.toString().isEmpty()) {
            future = self.getPeer().bootstrap().peerAddress(bootstrapPeerAddress)
                .start();
        } else {
            // TODO wrong
            future = self.getPeer().bootstrap()
                .start();
        }

        try {
            future.awaitListeners();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);

            return false;
        }

        return future.isSuccess();
    }

    @Override
    public void logout() {
        self.getPeer().announceShutdown();
        self.getPeer().shutdown();

        self = null;
    }

    @Override
    public PeerObject getSelf() {
        return self;
    }

    @Override
    public PeerObject getPeer(PeerAddress peerAddress) {
        throw new NotImplementedException();
    }
}
