package ch.hsr.dcc.infrastructure.db;

import ch.hsr.dcc.event.dbchanged.DbSavedEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbGatewayConfiguration {

    @Bean
    public DbGateway dbGateway(DbSavedEventPublisher dbSavedEventPublisher,
                               DbFriendRepository dbFriendRepository,
                               DbGroupRepository dbGroupRepository,
                               DbMessageRepository dbMessageRepository,
                               DbGroupMessageRepository dbGroupMessageRepository,
                               DbKeyStoreRepository dbKeyStoreRepository) {
        return new JpaDatabaseGateway(
            dbSavedEventPublisher,
            dbFriendRepository,
            dbGroupRepository,
            dbMessageRepository,
            dbGroupMessageRepository,
            dbKeyStoreRepository
        );
    }
}
