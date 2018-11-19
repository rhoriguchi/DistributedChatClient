package ch.hsr.view.chat.friendsbox;

import ch.hsr.domain.friend.Friend;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class FriendEntry {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendEntry.class);

    private final Friend friend;

    @FXML
    @Getter
    private HBox self;

    @FXML
    private Label usernameLabel;

    public FriendEntry(Friend friend) {
        this.friend = friend;

        // TODO use fx:controller="ch.hsr.view.chat.friendsbox.FriendEntry"
        // https://stackoverflow.com/questions/40047802/javafx-collection-binding-from-fxml-and-item-template
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat/friendsbox/friendEntry.fxml"));
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
    private void initialize() {
        usernameLabel.setText(friend.getFriend().getUsername().toString());
    }

}
