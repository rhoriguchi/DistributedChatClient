package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.tomp2p.PeerHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerHolderConfiguration {

    @Value ("${tomp2p.port:4000}")
    private int port;

    @Bean
    public PeerHolder peerHolder() {
        return new PeerHolder(port);
    }
}
