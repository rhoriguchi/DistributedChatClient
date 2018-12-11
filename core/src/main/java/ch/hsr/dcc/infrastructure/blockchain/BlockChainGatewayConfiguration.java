package ch.hsr.dcc.infrastructure.blockchain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class BlockChainGatewayConfiguration {

    //TODO find better name
    @Value ("${infura.token}")
    private String infuraToken;

    @Bean
    public BlockChainGateway blockChainGateway(Web3j web3j) {
        return new Web3jImplementation(web3j);
    }

    @Bean
    public Web3j web3j() {
        String url = String.format("https://rinkeby.infura.io/v3/%s", infuraToken);
        return Web3j.build(new HttpService(url));
    }
}
