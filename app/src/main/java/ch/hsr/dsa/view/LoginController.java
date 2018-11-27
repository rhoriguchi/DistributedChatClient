package ch.hsr.dsa.view;

import ch.hsr.dsa.application.PeerService;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.peer.IpAddress;
import ch.hsr.dsa.view.chat.friendsbox.FriendsBoxController;
import ch.hsr.dsa.view.chat.statusbox.StatusBoxController;
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
import org.springframework.stereotype.Controller;


@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final RootController rootController;
    private final StatusBoxController statusBoxController;
    private final FriendsBoxController friendsBoxController;

    private final PeerService peerService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField bootstrapPeerIpAddressTextField;

    @FXML
    private Button loginButton;

    public LoginController(RootController rootController,
                           StatusBoxController statusBoxController,
                           FriendsBoxController friendsBoxController,
                           PeerService peerService) {
        this.rootController = rootController;
        this.statusBoxController = statusBoxController;
        this.friendsBoxController = friendsBoxController;
        this.peerService = peerService;
    }

    @FXML
    private void initialize() {
        usernameTextField.textProperty().addListener(getButtonChangeListener());
        bootstrapPeerIpAddressTextField.textProperty().addListener(getButtonChangeListener());

        usernameTextField.setOnKeyPressed(getEnterEventHandler());
        bootstrapPeerIpAddressTextField.setOnKeyPressed(getEnterEventHandler());
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
                loginButton.setDisable(true);

                //TODO add some kind of spinner while loading

                //TODO don't block ui
                peerService.login(
                    IpAddress.fromString(bootstrapPeerIpAddressTextField.getText().trim()),
                    Username.fromString(usernameTextField.getText().trim())
                );
            } catch (IllegalArgumentException e) {
                //TODO do something whit this exception
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private ChangeListener<String> getButtonChangeListener() {
        return (observable, oldValue, newValue) -> {
            String username = usernameTextField.getText().trim();
            String bootstrapPeerIpAddress = bootstrapPeerIpAddressTextField.getText().trim();

            try {
                if (!username.isEmpty()) {
                    if (bootstrapPeerIpAddress.isEmpty()) {
                        loginButton.setDisable(false);
                    } else if (IpAddress.isValidIpAddress(bootstrapPeerIpAddress)) {
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

    //TODO show exception that get's caught be peerService
    public void loginFailed() {
        //TODO remove spinner
        loginButton.setDisable(false);
    }

    //TODO bad solution
    public void loginSuccess() {
        rootController.getLoginBox().setVisible(false);

        statusBoxController.updateSelf();
        //TODO ugly but need to not get null pointer
        friendsBoxController.updateFriendsListView();
        rootController.getChatBox().setVisible(true);
    }

    @FXML
    private void login(ActionEvent event) {
        login();
    }
}