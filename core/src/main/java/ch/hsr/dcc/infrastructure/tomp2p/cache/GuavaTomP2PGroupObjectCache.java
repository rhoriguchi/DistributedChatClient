package ch.hsr.dcc.infrastructure.tomp2p.cache;

import ch.hsr.dcc.infrastructure.exception.CacheException;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2PGroupObjectCache implements TomP2PGroupObjectCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2PGroupObjectCache.class);

    private final LoadingCache<Long, Optional<TomP2PGroupObject>> groupObjectCache;

    public GuavaTomP2PGroupObjectCache(TomP2P tomP2P, int capacity, int duration) {
        groupObjectCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, Optional<TomP2PGroupObject>>() {
                @Override
                public Optional<TomP2PGroupObject> load(Long id) {
                    return tomP2P.getGroupObject(id);
                }
            });
    }

    @Override
    public Optional<TomP2PGroupObject> get(Long id) {
        try {
            return groupObjectCache.get(id);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CacheException(String.format("Get groupObject in cache with id %s failed", id));
        }
    }

    @Override
    public void invalidate(Long id) {
        groupObjectCache.invalidate(id);
    }
}
