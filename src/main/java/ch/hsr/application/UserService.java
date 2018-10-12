package ch.hsr.application;

import ch.hsr.domain.user.PeerAddress;
import ch.hsr.domain.user.Username;
import ch.hsr.mapping.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(PeerAddress bootstrapPeerAddress, Username username) {
        userRepository.login(bootstrapPeerAddress, username);
    }
}
