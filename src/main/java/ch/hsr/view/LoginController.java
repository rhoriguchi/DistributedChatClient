package ch.hsr.view;

import ch.hsr.application.PeerService;
import ch.hsr.domain.peer.Username;
import ch.hsr.domain.peer.peeraddress.PeerAddress;
import ch.hsr.view.chat.statusbox.StatusBoxController;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final RootController rootController;
    private final StatusBoxController statusBoxController;

    private final PeerService peerService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField bootstrapPeerAddressTextField;

    @FXML
    private Button loginButton;

    public LoginController(RootController rootController, StatusBoxController statusBoxController, PeerService peerService) {
        this.rootController = rootController;
        this.statusBoxController = statusBoxController;
        this.peerService = peerService;
    }

    @FXML
    private void initialize() {
        usernameTextField.textProperty().addListener(getButtonChangeListener());
        bootstrapPeerAddressTextField.textProperty().addListener(getButtonChangeListener());

        usernameTextField.setOnKeyPressed(getEnterEventHandler());
        bootstrapPeerAddressTextField.setOnKeyPressed(getEnterEventHandler());
    }

    private EventHandler<KeyEvent> getEnterEventHandler() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER && !usernameTextField.getText().trim().isEmpty()) {
                login();
            }
        };
    }

    private void login() {
        if (!loginButton.isDisable()) {
            try {
                // TODO add some kind of spinner while loading

                PeerAddress peerAddress;
                try {
                    peerAddress = PeerAddress.fromSerialize(bootstrapPeerAddressTextField.getText().trim());
                } catch (IllegalArgumentException e) {
                    peerAddress = PeerAddress.empty();
                    LOGGER.error(e.getMessage(), e);
                }

                boolean success = peerService.login(
                    peerAddress,
                    Username.fromString(usernameTextField.getText().trim())
                );

                if (success) {
                    // TODO bad solution
                    rootController.getLoginBox().setVisible(false);

                    statusBoxController.updateSelf();
                    rootController.getChatBox().setVisible(true);
                } else {
                    // TODO throw exception or something
                }
            } catch (IllegalArgumentException e) {
                // TODO do something whit this exception
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private ChangeListener<String> getButtonChangeListener() {
        return (observable, oldValue, newValue) -> {
            String username = usernameTextField.getText().trim();
            String bootstrapPeerAddress = bootstrapPeerAddressTextField.getText().trim();

            try {
                if (!username.isEmpty()) {
                    if (bootstrapPeerAddress.isEmpty()) {
                        loginButton.setDisable(false);
                    } else if (!PeerAddress.fromSerialize(bootstrapPeerAddress).isEmpty()) {
                        loginButton.setDisable(false);
                    } else {
                        loginButton.setDisable(true);
                    }
                } else {
                    loginButton.setDisable(true);
                }
            } catch (RuntimeException e) {
                loginButton.setDisable(true);
                LOGGER.error(e.getMessage(), e);
            }
        };
    }

    @FXML
    private void login(ActionEvent event) {
        login();
    }
}