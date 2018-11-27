package ch.hsr.dsa.view;

import ch.hsr.dsa.event.login.LoginEvent;
import javafx.application.Platform;
import org.springframework.context.event.EventListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LoginEventListener {

    private final LoginController loginController;

    public LoginEventListener(LoginController loginController) {
        this.loginController = loginController;
    }

    @EventListener
    public void dbFriendSaved(LoginEvent loginEvent) {
        if (loginEvent.isFailed()) {
            Platform.runLater(loginController::loginFailed);
        } else {
            Platform.runLater(loginController::loginSuccess);
        }
    }
}
