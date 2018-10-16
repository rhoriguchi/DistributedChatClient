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

    private final RootController rootController;

    private final UserService userService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField bootstrapPeerAddressTextField;

    @FXML
    private Button loginButton;

    public LoginController(RootController rootController, UserService userService) {
        this.rootController = rootController;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        usernameTextField.textProperty().addListener(getButtonChangeListener());
        bootstrapPeerAddressTextField.textProperty().addListener(getButtonChangeListener());

        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !usernameTextField.getText().trim().isEmpty()) {
                login();
            }
        });
    }

    private ChangeListener<String> getButtonChangeListener() {
        return (observable, oldValue, newValue) -> {
            String username = usernameTextField.getText().trim();
            String bootstrapPeerAddress = bootstrapPeerAddressTextField.getText().trim();

            if ((bootstrapPeerAddress.isEmpty() || PeerAddress.fromSerialize(bootstrapPeerAddress) != null)
                && !username.isEmpty()) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        };
    }

    private void login() {
        String username = usernameTextField.getText().trim();
        String bootstrapPeerAddress = bootstrapPeerAddressTextField.getText().trim();

        if (!username.isEmpty() && !bootstrapPeerAddress.isEmpty()) {
            // TODO add some kind of spinner while loading

            boolean success = userService.login(
                PeerAddress.fromSerialize(bootstrapPeerAddress),
                Username.fromString(username)
            );

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