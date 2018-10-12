package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.DistributedHashTable;
import ch.hsr.infrastructure.tomp2p.TomP2P;
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
    public DistributedHashTable distributedHashTable() {
        return new TomP2P(port, maxActionWaitTime);
    }

}
