package ch.hsr.dcc.infrastructure.blockchain;

import org.web3j.crypto.Credentials;

public interface BlockChainGateway {

    void executeNotarizeHashTransaction(Credentials credentials, byte[] hash);

    boolean verifyNotarizedHash(Credentials credentials, byte[] hash);
}
