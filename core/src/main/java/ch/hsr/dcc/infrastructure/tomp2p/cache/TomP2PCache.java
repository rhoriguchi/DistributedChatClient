package ch.hsr.dcc.infrastructure.tomp2p.cache;

import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupAdd;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PPeerAddress;
import java.net.Inet4Address;
import java.util.Optional;

public class TomP2PCache implements TomP2P {

    private final TomP2P tomP2P;

    private final TomP2PPeerObjectCache tomP2PPeerObjectCache;
    private final TomP2PGroupObjectCache tomP2PGroupObjectCache;

    public TomP2PCache(TomP2P tomP2P,
                       TomP2PPeerObjectCache tomP2PPeerObjectCache,
                       TomP2PGroupObjectCache tomP2PGroupObjectCache) {
        this.tomP2P = tomP2P;
        this.tomP2PPeerObjectCache = tomP2PPeerObjectCache;
        this.tomP2PGroupObjectCache = tomP2PGroupObjectCache;
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
    public TomP2PPeerObject getSelf() {
        return tomP2P.getSelf();
    }

    @Override
    public void sendMessage(TomP2PMessage tomP2PMessage, TomP2PPeerAddress tomP2PPeerAddress) {
        tomP2P.sendMessage(tomP2PMessage, tomP2PPeerAddress);
    }

    @Override
    public void sendFriendRequest(TomP2PFriendRequest tomP2PFriendRequest, TomP2PPeerAddress tomP2PPeerAddress) {
        tomP2P.sendFriendRequest(tomP2PFriendRequest, tomP2PPeerAddress);
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
    public Optional<TomP2PPeerObject> getPeerObject(String username) {
        return tomP2PPeerObjectCache.get(username);
    }

    @Override
    public TomP2PFriendRequest getOldestReceivedTomP2PFriendRequest() {
        return tomP2P.getOldestReceivedTomP2PFriendRequest();
    }

    @Override
    public Optional<TomP2PGroupObject> getGroupObject(Long id) {
        return tomP2PGroupObjectCache.get(id);
    }

    @Override
    public void addGroupObject(TomP2PGroupObject tomP2PGroupObject) {
        tomP2P.addGroupObject(tomP2PGroupObject);
        tomP2PGroupObjectCache.invalidate(tomP2PGroupObject.getId());
    }

    @Override
    public void sendGroupAdd(TomP2PGroupAdd groupToTomP2PGroupAdd, TomP2PPeerAddress tomP2PPeerAddress) {
        tomP2P.sendGroupAdd(groupToTomP2PGroupAdd, tomP2PPeerAddress);
    }

    @Override
    public TomP2PGroupAdd getOldestReceivedTomP2PGroupAdd() {
        return tomP2P.getOldestReceivedTomP2PGroupAdd();
    }
}
