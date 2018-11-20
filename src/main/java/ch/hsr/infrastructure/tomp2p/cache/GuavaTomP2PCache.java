package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.tomp2p.PeerObject;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.message.DefaultTomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.peers.Number160;
import java.net.Inet4Address;
import java.util.Optional;

public class GuavaTomP2PCache implements TomP2P {

    private final TomP2P tomP2P;

    private final GuavaTomP2PPublicKeyCache guavaTomP2PPublicKeyCache;
    private final GuavaTomP2PUsernameCache guavaTomP2PUsernameCache;
    private final GuavaTomP2POnlineStateCache guavaTomP2POnlineStateCache;
    private final GuavaTomP2PPeerCache guavaTomP2PPeerCache;

    public GuavaTomP2PCache(TomP2P tomP2P,
                            GuavaTomP2PPublicKeyCache guavaTomP2PPublicKeyCache,
                            GuavaTomP2PUsernameCache guavaTomP2PUsernameCache,
                            GuavaTomP2POnlineStateCache guavaTomP2POnlineStateCache,
                            GuavaTomP2PPeerCache guavaTomP2PPeerCache) {
        this.tomP2P = tomP2P;
        this.guavaTomP2PPublicKeyCache = guavaTomP2PPublicKeyCache;
        this.guavaTomP2PUsernameCache = guavaTomP2PUsernameCache;
        this.guavaTomP2POnlineStateCache = guavaTomP2POnlineStateCache;
        this.guavaTomP2PPeerCache = guavaTomP2PPeerCache;
    }

    @Override
    public void login(Inet4Address bootstrapInet4Address, String username) {
        tomP2P.login(bootstrapInet4Address, username);
    }

    @Override
    public void login(String username) {
        tomP2P.login(username);
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
    public Optional<String> getPublicKey(String username) {
        return guavaTomP2PPublicKeyCache.get(username);
    }

    @Override
    public void savePublicKey(String username, String publicKey) {
        tomP2P.savePublicKey(username, publicKey);
    }

    @Override
    public Optional<String> getUserName(Number160 peerId) {
        return guavaTomP2PUsernameCache.get(peerId);
    }

    @Override
    public boolean isOnline(Number160 peerId) {
        return guavaTomP2POnlineStateCache.get(peerId);
    }

    @Override
    public Optional<PeerObject> getPeerObject(String username) {
        return guavaTomP2PPeerCache.get(username);
    }
}
