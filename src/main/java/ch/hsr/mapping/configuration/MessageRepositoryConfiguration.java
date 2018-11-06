package ch.hsr.mapping.configuration;

import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.mapping.message.MessageMapper;
import ch.hsr.mapping.message.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRepositoryConfiguration {

    @Bean
    public MessageRepository messageRepository(DbGateway dbGateway, TomP2P tomP2P) {
        return new MessageMapper(dbGateway, tomP2P);
    }
}
