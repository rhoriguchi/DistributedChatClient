package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.tomp2p.TomP2P;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2POnlineStateCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2POnlineStateCache.class);

    private final LoadingCache<Number160, Boolean> onlineStateCache;

    public GuavaTomP2POnlineStateCache(TomP2P tomP2P, int capacity, int duration) {
        onlineStateCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<Number160, Boolean>() {
                @Override
                public Boolean load(Number160 peerId) {
                    return tomP2P.isOnline(peerId);
                }
            });
    }

    public boolean get(Number160 peerId) {
        try {
            return onlineStateCache.get(peerId);
        } catch (ExecutionException e) {
            LOGGER.error(String.format("Get online state in cache with peerId %s failed", peerId), e);
            // TODO wrong exception type
            throw new IllegalArgumentException(String.format("Get publicKey in cache with username %s failed",
                peerId));
        }
    }
}
