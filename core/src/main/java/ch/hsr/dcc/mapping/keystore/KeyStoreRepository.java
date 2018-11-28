package ch.hsr.dcc.mapping.keystore;

import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.keystore.PubKey;
import ch.hsr.dcc.domain.keystore.Sign;
import ch.hsr.dcc.domain.keystore.SignState;

public interface KeyStoreRepository {

    PubKey getPubKeyFromDb(Username username);

    Sign sign(int hashCode);

    SignState CheckSignature(Username username, Sign sign, int hashCode);
}
