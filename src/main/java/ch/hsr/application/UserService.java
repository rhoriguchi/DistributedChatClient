package ch.hsr.application;

import ch.hsr.domain.user.Username;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {


    public UserService() {

    }

    public void login(Username username) {
        throw new NotImplementedException();
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
