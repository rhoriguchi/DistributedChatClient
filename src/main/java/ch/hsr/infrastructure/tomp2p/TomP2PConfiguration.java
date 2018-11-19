package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.cache.GuavaTomP2PCache;
import ch.hsr.infrastructure.tomp2p.cache.GuavaTomP2POnlineStateCache;
import ch.hsr.infrastructure.tomp2p.cache.GuavaTomP2PPeerDHTCache;
import ch.hsr.infrastructure.tomp2p.cache.GuavaTomP2PPublicKeyCache;
import ch.hsr.infrastructure.tomp2p.cache.GuavaTomP2PUsernameCache;
import ch.hsr.infrastructure.tomp2p.message.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PConfiguration {

    @Value ("${tomp2p.cache.enable:true}")
    private boolean cacheEnabled;

    @Value ("${tomp2p.cache.peerdht.capacity:1000}")
    private int peerDHTCapacity;

    @Value ("${tomp2p.cache.peerdht.duration:300}")
    private int peerDHTDuration;

    @Value ("${tomp2p.cache.username.capacity:1000}")
    private int usernameCapacity;

    @Value ("${tomp2p.cache.username.duration:300}")
    private int usernameDuration;

    @Value ("${tomp2p.cache.publickey.capacity:1000}")
    private int publicKeyCapacity;

    @Value ("${tomp2p.cache.publickey.duration:300}")
    private int publicKeyDuration;

    @Value ("${tomp2p.cache.onlinestate.capacity:1000}")
    private int onlineStateCapacity;

    @Value ("${tomp2p.cache.onlinestate.duration:60}")
    private int onlineStateDuration;

    @Bean
    public TomP2P tomP2P(PeerHolder peerHolder, DHTHandler dhtHandler, MessageHandler messageHandler) {
        TomP2P tomP2P = getTomP2P(peerHolder, dhtHandler, messageHandler);

        if (cacheEnabled) {
            return getGuavaTomP2PCache(tomP2P);
        } else {
            return tomP2P;
        }
    }

    private GuavaTomP2PCache getGuavaTomP2PCache(TomP2P tomP2P) {
        GuavaTomP2PPublicKeyCache guavaTomP2PPublicKeyCache = getGuavaTomP2PPublicKeyCache(tomP2P);
        GuavaTomP2PUsernameCache guavaTomP2PUsernameCache = getGuavaTomP2PUsernameCache(tomP2P);
        GuavaTomP2POnlineStateCache guavaTomP2POnlineStateCache = getGuavaTomP2POnlineCache(tomP2P);
        GuavaTomP2PPeerDHTCache guavaTomP2PPeerDHTCache = getGuavaTomP2PPeerDHTCache(tomP2P);

        return new GuavaTomP2PCache(
            tomP2P,
            guavaTomP2PPublicKeyCache,
            guavaTomP2PUsernameCache,
            guavaTomP2POnlineStateCache,
            guavaTomP2PPeerDHTCache
        );
    }

    private GuavaTomP2PPeerDHTCache getGuavaTomP2PPeerDHTCache(TomP2P tomP2P) {
        return new GuavaTomP2PPeerDHTCache(tomP2P, peerDHTCapacity, peerDHTDuration);
    }

    private GuavaTomP2PUsernameCache getGuavaTomP2PUsernameCache(TomP2P tomP2P) {
        return new GuavaTomP2PUsernameCache(tomP2P, usernameCapacity, usernameDuration);
    }

    private GuavaTomP2PPublicKeyCache getGuavaTomP2PPublicKeyCache(TomP2P tomP2P) {
        return new GuavaTomP2PPublicKeyCache(tomP2P, publicKeyCapacity, publicKeyDuration);
    }

    private GuavaTomP2POnlineStateCache getGuavaTomP2POnlineCache(TomP2P tomP2P) {
        return new GuavaTomP2POnlineStateCache(tomP2P, onlineStateCapacity, onlineStateDuration);
    }

    private TomP2P getTomP2P(PeerHolder peerHolder, DHTHandler dhtHandler, MessageHandler messageHandler) {
        return new TomP2PImplementation(peerHolder, dhtHandler, messageHandler);
    }
}
