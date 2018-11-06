package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.TomP2PImplementation;
import net.tomp2p.p2p.RequestP2PConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PConfiguration {

    @Value ("${tomp2p.port:4000}")
    private int port;

    @Value ("${tomp2p.send.minimumResult:1}")
    private int sendMinimumResult;

    @Value ("${tomp2p.send.maxFailure:10}")
    private int sendMaxFailure;

    @Value ("${tomp2p.send.parallelDiff:0}")
    private int sendParallelDiff;

    @Bean
    public TomP2P tomP2P(RequestP2PConfiguration requestP2PConfiguration) {
        return new TomP2PImplementation(requestP2PConfiguration, port);
    }

    @Bean
    public RequestP2PConfiguration requestP2PConfiguration() {
        return new RequestP2PConfiguration(sendMinimumResult, sendMaxFailure, sendParallelDiff);
    }
}
