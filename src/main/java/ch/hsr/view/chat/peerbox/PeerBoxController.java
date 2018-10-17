package ch.hsr.view.chat.peerbox;

import ch.hsr.application.PeerService;
import ch.hsr.domain.peer.Peer;
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

    private final PeerService peerService;

    @FXML
    private ListView<Peer> peerListView;
    private ObservableList<Peer> observableList = FXCollections.observableArrayList();

    public PeerBoxController(MessageBoxController messageBoxController, PeerService peerService) {
        this.messageBoxController = messageBoxController;
        this.peerService = peerService;
    }

    @FXML
    protected void initialize() {
        observableList.setAll(peerService.getPeer());

        peerListView.setItems(observableList);
        peerListView.setCellFactory(listView -> new ListCell<Peer>() {
            @Override
            public void updateItem(Peer peer, boolean empty) {
                super.updateItem(peer, empty);
                if (peer != null) {
                    PeerEntry peerEntry = new PeerEntry(peer);
                    setGraphic(peerEntry.getSelf());
                }
            }
        });

        peerListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> messageBoxController.changeToPeer(newValue));
    }
}