package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.tomp2p.DistributedHashTable;
import ch.hsr.mapping.user.UserMapper;
import ch.hsr.mapping.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMapperConfiguration {

    @Bean
    public UserRepository userRepository(DistributedHashTable distributedHashTable) {
        return new UserMapper(distributedHashTable);
    }
}
