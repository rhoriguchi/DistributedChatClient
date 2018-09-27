package ch.hsr.application;

import ch.hsr.domain.user.Username;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class LoginService {


    public LoginService() {

    }

    public void login(Username username) {
        throw new NotImplementedException();
    }
}
