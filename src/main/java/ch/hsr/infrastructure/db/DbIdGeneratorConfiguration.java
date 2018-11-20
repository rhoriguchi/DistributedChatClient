package ch.hsr.infrastructure.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbIdGeneratorConfiguration {

    @Bean
    public DbIdGenerator dbKeyGenerator() {
        return new DbIdGenerator();
    }
}
