package ch.hsr.view.chat.peerbox;

import ch.hsr.domain.user.Username;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class PeerEntry {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerEntry.class);

    private final Username username;

    @FXML
    @Getter
    private HBox self;

    @FXML
    private Label usernameLabel;

    public PeerEntry(Username username) {
        this.username = username;

        // TODO use fx:controller="ch.hsr.view.chat.peerbox.PeerEntry"
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat/peerbox/peerEntry.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            // TODO correct exception
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void initialize() {
        usernameLabel.setText(username.toString());
    }

}
