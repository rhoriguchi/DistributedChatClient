package ch.hsr.view;

import ch.hsr.application.UserService;
import ch.hsr.domain.user.Username;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.springframework.stereotype.Component;


@Component
public class LoginController {

    private final RootController rootController;

    private final UserService userService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    public LoginController(RootController rootController, UserService userService) {
        this.rootController = rootController;
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

        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                login();
            }
        });
    }

    private void login() {
        String username = usernameTextField.getText();
        if (!username.isEmpty()) {
            // TODO add some kind of spinner while loading
            // TODO max time while loading
            boolean success = userService.login(Username.fromString(username));

            if (success) {
                // TODO bad solution
                rootController.getLoginBox().setVisible(false);
                rootController.getChatBox().setVisible(true);
            } else {
                // TODO throw exception or something
            }
        }
    }

    @FXML
    private void login(ActionEvent event) {
        login();
    }
}