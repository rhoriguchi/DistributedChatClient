package ch.hsr.dcc.infrastructure.tomp2p.cache;

import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import java.util.Optional;

public interface TomP2PGroupObjectCache {

    Optional<TomP2PGroupObject> get(Long id);

    void invalidate(Long id);
}
