package ch.hsr.dcc.view.listener;

import ch.hsr.dcc.event.login.LoginEvent;
import ch.hsr.dcc.view.LoginController;
import javafx.application.Platform;
import org.springframework.context.event.EventListener;

public class LoginEventListener {

    private final LoginController loginController;

    public LoginEventListener(LoginController loginController) {
        this.loginController = loginController;
    }

    @EventListener
    public void login(LoginEvent loginEvent) {
        if (loginEvent.isFailed()) {
            Platform.runLater(loginController::loginFailed);
        } else {
            Platform.runLater(loginController::loginSuccess);
        }
    }
}
