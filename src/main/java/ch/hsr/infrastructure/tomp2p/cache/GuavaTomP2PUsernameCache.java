package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.exception.CacheException;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2PUsernameCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2PUsernameCache.class);

    private final LoadingCache<Number160, Optional<String>> usernameCache;

    public GuavaTomP2PUsernameCache(TomP2P tomP2P, int capacity, int duration) {
        usernameCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<Number160, Optional<String>>() {
                @Override
                public Optional<String> load(Number160 peerId) {
                    return tomP2P.getUserName(peerId);
                }
            });
    }

    public Optional<String> get(Number160 peerId) {
        try {
            return usernameCache.get(peerId);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CacheException(String.format("Get username in cache with peerId %s failed", peerId));
        }
    }

}
