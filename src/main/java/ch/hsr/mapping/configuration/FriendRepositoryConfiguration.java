package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.mapping.friend.FriendMapper;
import ch.hsr.mapping.friend.FriendRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendRepositoryConfiguration {

    @Bean
    public FriendRepository friendRepository(DbGateway dbGateway) {
        return new FriendMapper(dbGateway);
    }
}
