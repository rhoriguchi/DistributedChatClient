package ch.hsr.infrastructure.tomp2p.cache;

import ch.hsr.infrastructure.exception.CacheException;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTomP2PPublicKeyCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTomP2PPublicKeyCache.class);

    private final LoadingCache<String, String> publicKeyCache;

    public GuavaTomP2PPublicKeyCache(TomP2P tomP2P, int capacity, int duration) {
        publicKeyCache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .expireAfterWrite(duration, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String username) {
                    return tomP2P.getPublicKey(username);
                }
            });
    }

    public String get(String username) {
        try {
            return publicKeyCache.get(username);
        } catch (ExecutionException e) {
            LOGGER.error(String.format("Get publicKey in cache with username %s failed", username), e);
            throw new CacheException(String.format("Get publicKey in cache with username %s failed",
                username));
        }
    }

}
