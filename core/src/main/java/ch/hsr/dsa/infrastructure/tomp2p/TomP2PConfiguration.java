package ch.hsr.dsa.infrastructure.tomp2p;

import ch.hsr.dsa.infrastructure.tomp2p.cache.GuavaTomP2PCache;
import ch.hsr.dsa.infrastructure.tomp2p.cache.GuavaTomP2PPeerObjectCache;
import ch.hsr.dsa.infrastructure.tomp2p.dht.DHTHandler;
import ch.hsr.dsa.infrastructure.tomp2p.dht.DHTScheduler;
import ch.hsr.dsa.infrastructure.tomp2p.message.MessageHandler;
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

    @Bean
    public TomP2P tomP2P(PeerHolder peerHolder,
                         DHTHandler dhtHandler,
                         DHTScheduler dhtScheduler,
                         MessageHandler messageHandler) {
        TomP2P tomP2P = getTomP2P(peerHolder, dhtHandler, dhtScheduler, messageHandler);

        if (cacheEnabled) {
            return getGuavaTomP2PCache(tomP2P);
        } else {
            return tomP2P;
        }
    }

    private GuavaTomP2PCache getGuavaTomP2PCache(TomP2P tomP2P) {
        GuavaTomP2PPeerObjectCache guavaTomP2PPeerObjectCache = getGuavaTomP2PPeerCache(tomP2P);

        return new GuavaTomP2PCache(
            tomP2P,
            guavaTomP2PPeerObjectCache
        );
    }

    private GuavaTomP2PPeerObjectCache getGuavaTomP2PPeerCache(TomP2P tomP2P) {
        return new GuavaTomP2PPeerObjectCache(tomP2P, peerCapacity, peerDuration);
    }

    private TomP2P getTomP2P(PeerHolder peerHolder,
                             DHTHandler dhtHandler,
                             DHTScheduler dhtScheduler,
                             MessageHandler messageHandler) {
        return new TomP2PImplementation(peerHolder, dhtHandler, dhtScheduler, messageHandler);
    }
}
