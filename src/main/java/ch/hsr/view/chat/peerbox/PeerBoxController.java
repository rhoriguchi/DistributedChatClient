package ch.hsr.view.chat.peerbox;

import ch.hsr.application.UserService;
import ch.hsr.domain.user.Username;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

// TODO maybe search field
// TODO scheduler to periodically get new peers and add them
@Component
public class PeerBoxController {

    private final UserService userService;

    @FXML
    private ListView<Username> peerList;
    private ObservableList<Username> observableList = FXCollections.observableArrayList();

    public PeerBoxController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    protected void initialize() {
        observableList.setAll(userService.getUsers());
        peerList.setItems(observableList);
        peerList.setCellFactory(listView -> new PeerList());
    }
}