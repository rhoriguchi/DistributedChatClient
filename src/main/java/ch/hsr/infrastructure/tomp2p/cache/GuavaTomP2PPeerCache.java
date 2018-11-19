package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.tomp2p.PeerObject;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2PPeerCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2PPeerCache.class);

    private final LoadingCache<String, PeerObject> peerDHTCache;

    public GuavaTomP2PPeerCache(TomP2P tomP2P, int capacity, int duration) {
        peerDHTCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<String, PeerObject>() {
                @Override
                public PeerObject load(String username) {
                    return tomP2P.getPeerObject(username);
                }
            });
    }

    public PeerObject get(String username) {
        try {
            return peerDHTCache.get(username);
        } catch (ExecutionException e) {
            LOGGER.error(String.format("Get peerDHT in cache with username %s failed", username), e);
            // TODO wrong exception type
            throw new IllegalArgumentException(String.format("Get peerDHT in cache with username %s failed", username));
        }
    }
}
