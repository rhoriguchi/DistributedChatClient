package ch.hsr.application;

import ch.hsr.domain.user.Username;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {


    public UserService() {

    }

    // TODO return success or something, probably use timer aswell after certain time return false
    public boolean login(Username username) {
        return true;
    }

    // TODO get real user object collection
    public Set<Username> getUsers() {
        return IntStream.rangeClosed(0, 100)
            .mapToObj(String::valueOf)
            .map(Username::fromString)
            .collect(Collectors.toSet());
    }

    // TODO get real user object
    public Username getSelf() {
        return Username.fromString("Mock");
    }
}
