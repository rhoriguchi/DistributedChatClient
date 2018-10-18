package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.TomP2PImplementation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomP2PConfiguration {

    @Value ("${tomp2p.port:4000}")
    private int port;

    @Value ("${tomp2p.maxActionWaitTime:5000}")
    private int maxActionWaitTime;

    @Bean
    public TomP2P distributedHashTable() {
        return new TomP2PImplementation(port, maxActionWaitTime);
    }

}
