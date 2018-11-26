package ch.hsr.dsa.mapping.keystore;

import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.keystore.PubKey;
import ch.hsr.dsa.domain.keystore.Sign;
import ch.hsr.dsa.domain.keystore.SignState;

public interface KeyStoreRepository {

    PubKey getPubKeyFromDb(Username username);

    Sign sign(int hashCode);

    SignState CheckSignature(Username username, Sign sign, int hashCode);
}
