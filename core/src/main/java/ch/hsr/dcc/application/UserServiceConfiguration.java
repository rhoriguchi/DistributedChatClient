package ch.hsr.dcc.application;

import ch.hsr.dcc.mapping.friend.FriendRepository;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class UserServiceConfiguration {

    @Bean
    public UserService userService(FriendRepository friendRepository, PeerRepository peerRepository, KeyStoreRepository keyStoreRepository) {
        return new UserService(friendRepository, peerRepository, keyStoreRepository);
    }
}
