package ch.hsr.mapping.keystore;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.keystore.Sign;
import ch.hsr.domain.keystore.SignState;

public interface KeyStoreRepository {

    Sign sign(int hashCode);

    SignState CheckSignature(Username username, Sign sign, int hashCode);
}
