package ch.hsr.application.configuration;

import ch.hsr.application.FriendService;
import ch.hsr.mapping.friend.FriendRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendServiceConfiguration {

    @Bean
    public FriendService friendService(FriendRepository friendRepository, PeerRepository peerRepository) {
        return new FriendService(friendRepository, peerRepository);
    }
}
