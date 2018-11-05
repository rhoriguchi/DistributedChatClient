package ch.hsr.view.chat.friendsbox;

import ch.hsr.application.PeerService;
import ch.hsr.domain.peer.Peer;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class FriendsBoxController {

    private final MessageBoxController messageBoxController;

    private final PeerService peerService;

    @FXML
    // TODO not used
    public TextField addTextField;

    @FXML
    // TODO not used
    public Button addButton;

    @FXML
    private ListView<Peer> friendsListView;
    private ObservableList<Peer> observableList = FXCollections.observableArrayList();

    public FriendsBoxController(MessageBoxController messageBoxController, PeerService peerService) {
        this.messageBoxController = messageBoxController;
        this.peerService = peerService;
    }

    @FXML
    protected void initialize() {
        observableList.setAll(peerService.getPeers());

        friendsListView.setItems(observableList);
        friendsListView.setCellFactory(listView -> new ListCell<Peer>() {
            @Override
            public void updateItem(Peer peer, boolean empty) {
                super.updateItem(peer, empty);
                if (peer != null) {
                    FriendEntry friendEntry = new FriendEntry(peer);
                    setGraphic(friendEntry.getSelf());
                }
            }
        });

        friendsListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> messageBoxController.changeToPeer(newValue));
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        // TODO empty
    }
}