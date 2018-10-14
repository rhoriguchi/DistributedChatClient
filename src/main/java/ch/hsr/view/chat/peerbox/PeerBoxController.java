package ch.hsr.view.chat.peerbox;

import ch.hsr.application.UserService;
import ch.hsr.domain.user.Username;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

// TODO maybe search field
// TODO scheduler to periodically get new peers and add them
@Component
public class PeerBoxController {

    private final MessageBoxController messageBoxController;

    private final UserService userService;

    @FXML
    private ListView<Username> peerListView;
    private ObservableList<Username> observableList = FXCollections.observableArrayList();

    public PeerBoxController(MessageBoxController messageBoxController, UserService userService) {
        this.messageBoxController = messageBoxController;
        this.userService = userService;
    }

    @FXML
    protected void initialize() {
        observableList.setAll(userService.getUsers());

        peerListView.setItems(observableList);
        peerListView.setCellFactory(listView -> new ListCell<Username>() {
            @Override
            public void updateItem(Username username, boolean empty) {
                super.updateItem(username, empty);
                if (username != null && username.nonEmpty()) {
                    PeerEntry peerEntry = new PeerEntry(username);
                    setGraphic(peerEntry.getSelf());
                }
            }
        });

        peerListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> messageBoxController.changeToUsername(newValue.toString()));
    }
}