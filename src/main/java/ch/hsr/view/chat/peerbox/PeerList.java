package ch.hsr.view.chat.peerbox;

import ch.hsr.domain.user.Username;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class PeerList extends ListCell<Username> {

    @Override
    public void updateItem(Username username, boolean empty) {
        super.updateItem(username, empty);
        if (username != null && username.nonEmpty()) {
            PeerEntry peerEntry = new PeerEntry(username);
            setGraphic(peerEntry.getSelf());
        }
    }
}
