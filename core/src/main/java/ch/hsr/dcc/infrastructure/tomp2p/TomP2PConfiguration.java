package ch.hsr.dcc.infrastructure.tomp2p;

import ch.hsr.dcc.infrastructure.tomp2p.cache.GuavaTomP2PGroupObjectCache;
import ch.hsr.dcc.infrastructure.tomp2p.cache.GuavaTomP2PPeerObjectCache;
import ch.hsr.dcc.infrastructure.tomp2p.cache.TomP2PCache;
import ch.hsr.dcc.infrastructure.tomp2p.cache.TomP2PGroupObjectCache;
import ch.hsr.dcc.infrastructure.tomp2p.cache.TomP2PPeerObjectCache;
import ch.hsr.dcc.infrastructure.tomp2p.dht.DHTHandler;
import ch.hsr.dcc.infrastructure.tomp2p.dht.DHTScheduler;
import ch.hsr.dcc.infrastructure.tomp2p.message.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PConfiguration {

    @Value ("${tomp2p.cache.enable:true}")
    private boolean cacheEnabled;

    @Value ("${tomp2p.cache.peer.capacity:1000}")
    private int peerCapacity;

    @Value ("${tomp2p.cache.peer.duration:300}")
    private int peerDuration;

    @Value ("${tomp2p.cache.group.capacity:1000}")
    private int groupCapacity;

    @Value ("${tomp2p.cache.group.duration:300}")
    private int groupDuration;

    @Bean
    public TomP2P tomP2P(PeerHolder peerHolder,
                         DHTHandler dhtHandler,
                         DHTScheduler dhtScheduler,
                         MessageHandler messageHandler) {
        TomP2P tomP2P = getTomP2P(peerHolder, dhtHandler, dhtScheduler, messageHandler);

        if (cacheEnabled) {
            return getTomP2PCache(tomP2P);
        } else {
            return tomP2P;
        }
    }

    private TomP2PCache getTomP2PCache(TomP2P tomP2P) {
        TomP2PPeerObjectCache tomP2PPeerObjectCache = getTomP2PPeerObjectCache(tomP2P);
        TomP2PGroupObjectCache tomP2PGroupObjectCache = getTomP2PGroupObjectCache(tomP2P);

        return new TomP2PCache(
            tomP2P,
            tomP2PPeerObjectCache,
            tomP2PGroupObjectCache
        );
    }

    private TomP2PGroupObjectCache getTomP2PGroupObjectCache(TomP2P tomP2P) {
        return new GuavaTomP2PGroupObjectCache(tomP2P, groupCapacity, groupDuration);
    }

    private TomP2PPeerObjectCache getTomP2PPeerObjectCache(TomP2P tomP2P) {
        return new GuavaTomP2PPeerObjectCache(tomP2P, peerCapacity, peerDuration);
    }

    private TomP2P getTomP2P(PeerHolder peerHolder,
                             DHTHandler dhtHandler,
                             DHTScheduler dhtScheduler,
                             MessageHandler messageHandler) {
        return new TomP2PImplementation(peerHolder, dhtHandler, dhtScheduler, messageHandler);
    }
}
