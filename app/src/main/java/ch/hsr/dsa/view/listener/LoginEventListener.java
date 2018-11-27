package ch.hsr.dsa.view.listener;

import ch.hsr.dsa.event.login.LoginEvent;
import ch.hsr.dsa.view.LoginController;
import javafx.application.Platform;
import org.springframework.context.event.EventListener;

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
