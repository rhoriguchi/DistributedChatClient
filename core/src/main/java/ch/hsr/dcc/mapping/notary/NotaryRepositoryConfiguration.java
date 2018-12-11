package ch.hsr.dcc.mapping.notary;

import ch.hsr.dcc.infrastructure.blockchain.BlockChainGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotaryRepositoryConfiguration {

    @Value ("${privateKey}")
    private String privateKey;

    @Bean
    public NotaryRepository notaryRepository(BlockChainGateway blockChainGateway) {
        return new NotaryMapper(privateKey, blockChainGateway);
    }
}
