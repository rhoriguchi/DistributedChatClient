package ch.hsr.dcc.view;

import ch.hsr.dcc.application.PeerService;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.peer.IpAddress;
import ch.hsr.dcc.view.chat.friendsbox.FriendGroupBoxController;
import ch.hsr.dcc.view.chat.statusbox.StatusBoxController;
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
    private final FriendGroupBoxController friendGroupBoxController;

    private final PeerService peerService;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField bootstrapPeerIpAddressTextField;

    @FXML
    private Button loginButton;

    public LoginController(RootController rootController,
                           StatusBoxController statusBoxController,
                           FriendGroupBoxController friendGroupBoxController,
                           PeerService peerService) {
        this.rootController = rootController;
        this.statusBoxController = statusBoxController;
        this.friendGroupBoxController = friendGroupBoxController;
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
        friendGroupBoxController.updateFriendsListView();
        rootController.getChatBox().setVisible(true);
    }

    @FXML
    private void login(ActionEvent event) {
        login();
    }
}