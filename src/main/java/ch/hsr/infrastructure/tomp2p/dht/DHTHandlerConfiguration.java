package ch.hsr.infrastructure.tomp2p.dht;

import ch.hsr.infrastructure.tomp2p.PeerHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DHTHandlerConfiguration {

    @Value ("${tomp2p.dht.ttl:60}")
    private int ttl;

    @Value ("${tomp2p.dht.replicationInterval:1000}")
    private int replicationInterval;

    @Bean
    public DHTHandler dhtHandler(PeerHolder peerHolder) {
        return new DHTHandler(peerHolder, ttl, replicationInterval);
    }
}
