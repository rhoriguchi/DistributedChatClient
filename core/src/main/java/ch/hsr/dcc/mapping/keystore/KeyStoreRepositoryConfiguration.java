package ch.hsr.dcc.mapping.keystore;

import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
public class KeyStoreRepositoryConfiguration {

    @Value ("${rsa.keySize:2048}")
    private int RSAKeySize;

    @Bean
    public KeyStoreRepository keyStoreRepository(DbGateway dbGateway,
                                                 TomP2P tomP2P,
                                                 KeyPairGenerator keyPairGenerator,
                                                 KeyFactory keyFactory) {
        return new KeyStoreMapper(
            dbGateway,
            tomP2P,
            keyPairGenerator,
            keyFactory
        );
    }

    @Bean
    public KeyFactory keyFactory() {
        try {
            return KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("KeyFactory can't be initialized");
        }
    }

    @Bean
    public KeyPairGenerator keyPairGenerator() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(RSAKeySize);
            return keyPairGenerator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("KeyPairGenerator can't be initialized");
        }
    }
}
