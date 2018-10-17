package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.tomp2p.DistributedHashTable;
import ch.hsr.mapping.peer.PeerMapper;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeerMapperConfiguration {

    @Bean
    public PeerRepository userRepository(DistributedHashTable distributedHashTable) {
        return new PeerMapper(distributedHashTable);
    }
}
