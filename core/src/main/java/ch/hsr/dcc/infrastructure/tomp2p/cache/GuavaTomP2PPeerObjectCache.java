package ch.hsr.dcc.infrastructure.tomp2p.cache;

import ch.hsr.dcc.infrastructure.exception.CacheException;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2PPeerObjectCache implements TomP2PPeerObjectCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2PPeerObjectCache.class);

    private final LoadingCache<String, Optional<TomP2PPeerObject>> peerObjectCache;

    public GuavaTomP2PPeerObjectCache(TomP2P tomP2P, int capacity, int duration) {
        peerObjectCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Optional<TomP2PPeerObject>>() {
                @Override
                public Optional<TomP2PPeerObject> load(String username) {
                    return tomP2P.getPeerObject(username);
                }
            });
    }

    @Override
    public Optional<TomP2PPeerObject> get(String username) {
        try {
            return peerObjectCache.get(username);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CacheException(String.format("Get peerObject in cache with username %s failed", username));
        }
    }
}
