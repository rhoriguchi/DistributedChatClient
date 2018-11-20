package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.exception.CacheException;
import ch.hsr.infrastructure.tomp2p.PeerObject;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2PPeerCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2PPeerCache.class);

    private final LoadingCache<String, Optional<PeerObject>> peerDHTCache;

    public GuavaTomP2PPeerCache(TomP2P tomP2P, int capacity, int duration) {
        peerDHTCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Optional<PeerObject>>() {
                @Override
                public Optional<PeerObject> load(String username) {
                    return tomP2P.getPeerObject(username);
                }
            });
    }

    public Optional<PeerObject> get(String username) {
        try {
            return peerDHTCache.get(username);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CacheException(String.format("Get peerDHT in cache with username %s failed", username));
        }
    }
}
