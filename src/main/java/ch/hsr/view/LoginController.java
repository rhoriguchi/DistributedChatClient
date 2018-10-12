package ch.hsr.view;

import ch.hsr.application.UserService;
import ch.hsr.domain.user.PeerAddress;
import ch.hsr.domain.user.Username;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.springframework.stereotype.Component;


@Component
public class LoginController {

    private final UserService userService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField bootstrapPeerAddressTextField;

    @FXML
    private Button loginButton;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        usernameTextField.textProperty().addListener(getButtonChangeListener());
        bootstrapPeerAddressTextField.textProperty().addListener(getButtonChangeListener());

        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                login();
            }
        });
    }

    private ChangeListener<String> getButtonChangeListener() {
        return (observable, oldValue, newValue) -> {
            String username = usernameTextField.getText();
            String bootstrapPeerAddress = bootstrapPeerAddressTextField.getText();

            if ((bootstrapPeerAddress.isEmpty() || PeerAddress.fromSerialize(bootstrapPeerAddress) != null) && !username.isEmpty()) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        };
    }

    private void login() {
        Username username = Username.fromString(usernameTextField.getText());
        if (username.nonEmpty()) {
            PeerAddress peerAddress = PeerAddress.fromSerialize(bootstrapPeerAddressTextField.getText());

            userService.login(peerAddress, username);
        }

    }

    @FXML
    private void login(ActionEvent event) {
        login();
    }
}