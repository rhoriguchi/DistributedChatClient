package ch.hsr.infrastructure.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbGatewayConfiguration {

    @Bean
    public DbGateway dbGateway(DbFriendRepository dbFriendRepository,
                               DbGroupRepository dbGroupRepository,
                               DbMessageRepository dbMessageRepository,
                               DbGroupMessageRepository dbGroupMessageRepository,
                               DbKeyStoreRepository dbKeyStoreRepository,
                               DbIdGenerator dbIdGenerator) {
        return new JpaDatabaseGateway(
            dbFriendRepository,
            dbGroupRepository,
            dbMessageRepository,
            dbGroupMessageRepository,
            dbKeyStoreRepository,
            dbIdGenerator
        );
    }
}
