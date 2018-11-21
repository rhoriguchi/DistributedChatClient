package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.tomp2p.PeerObject;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.message.DefaultTomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import java.net.Inet4Address;
import java.util.Optional;

public class GuavaTomP2PCache implements TomP2P {

    private final TomP2P tomP2P;

    private final GuavaTomP2PPeerObjectCache guavaTomP2PPeerObjectCache;

    public GuavaTomP2PCache(TomP2P tomP2P,
                            GuavaTomP2PPeerObjectCache guavaTomP2PPeerObjectCache) {
        this.tomP2P = tomP2P;
        this.guavaTomP2PPeerObjectCache = guavaTomP2PPeerObjectCache;
    }

    @Override
    public void login(Inet4Address bootstrapInet4Address, String username, String publicKey) {
        tomP2P.login(bootstrapInet4Address, username, publicKey);
    }

    @Override
    public void login(String username, String publicKey) {
        tomP2P.login(username, publicKey);
    }

    @Override
    public void logout() {
        tomP2P.logout();
    }

    @Override
    public PeerObject getSelf() {
        return tomP2P.getSelf();
    }

    @Override
    public void sendMessage(DefaultTomP2PMessage defaultTomP2PMessage) {
        tomP2P.sendMessage(defaultTomP2PMessage);
    }

    @Override
    public TomP2PMessage getOldestReceivedTomP2PMessage() {
        return tomP2P.getOldestReceivedTomP2PMessage();
    }

    @Override
    public TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage() {
        return tomP2P.getOldestReceivedTomP2PGroupMessage();
    }

    @Override
    public Optional<PeerObject> getPeerObject(String username) {
        return guavaTomP2PPeerObjectCache.get(username);
    }
}
