package ch.hsr.infrastructure.configuration;

import ch.hsr.infrastructure.db.DbFriendRepository;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbGroupMessageRepository;
import ch.hsr.infrastructure.db.DbGroupRepository;
import ch.hsr.infrastructure.db.DbMessageRepository;
import ch.hsr.infrastructure.db.JpaDatabaseGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfiguration {

    @Bean
    public DbGateway dbGateway(DbFriendRepository dbFriendRepository,
                               DbGroupRepository dbGroupRepository,
                               DbMessageRepository dbMessageRepository,
                               DbGroupMessageRepository dbGroupMessageRepository) {
        return new JpaDatabaseGateway(
            dbFriendRepository,
            dbGroupRepository,
            dbMessageRepository,
            dbGroupMessageRepository
        );
    }
}
