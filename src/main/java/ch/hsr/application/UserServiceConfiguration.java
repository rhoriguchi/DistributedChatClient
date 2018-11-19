package ch.hsr.application;

import ch.hsr.application.UserService;
import ch.hsr.mapping.friend.FriendRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {

    @Bean
    public UserService userService(FriendRepository friendRepository, PeerRepository peerRepository) {
        return new UserService(friendRepository, peerRepository);
    }
}
