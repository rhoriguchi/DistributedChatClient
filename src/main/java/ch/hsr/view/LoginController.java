package ch.hsr.view;

import ch.hsr.application.UserService;
import ch.hsr.domain.user.Username;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;


@Component
public class LoginController {

    private final UserService userService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    protected void initialize() {
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loginButton.setDisable(true);
            } else {
                loginButton.setDisable(false);
            }
        });
    }

    @FXML
    private void login(ActionEvent event) {
        String username = usernameTextField.getText();

        if (!username.isEmpty()) {
            userService.login(Username.fromString(username));
        }
    }
}