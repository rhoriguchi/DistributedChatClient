package ch.hsr.mapping.keystore;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.keystore.Sign;

public interface KeyStoreRepository {

    // TODO use for hashcode domain object?

    Sign sign(int hashCode);

    boolean CheckSignature(Username username, Sign sign, int hashCode);
}
