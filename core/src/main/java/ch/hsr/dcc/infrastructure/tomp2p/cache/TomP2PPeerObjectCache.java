package ch.hsr.dcc.infrastructure.tomp2p.cache;

import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import java.util.Optional;

public interface TomP2PPeerObjectCache {

    Optional<TomP2PPeerObject> get(String username);
}
