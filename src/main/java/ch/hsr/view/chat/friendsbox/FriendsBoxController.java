package ch.hsr.view.chat.friendsbox;

import ch.hsr.application.FriendService;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Controller;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FriendsBoxController {

    private final MessageBoxController messageBoxController;

    private final FriendService friendService;

    @FXML
    public TextField addUsernameTextField;

    @FXML
    public Button addButton;

    @FXML
    private ListView<Friend> friendsListView;
    private ObservableList<Friend> observableList = FXCollections.observableArrayList();

    public FriendsBoxController(MessageBoxController messageBoxController, FriendService friendService) {
        this.messageBoxController = messageBoxController;
        this.friendService = friendService;
    }

    @FXML
    private void initialize() {
        initFriendsListView();

        addUsernameTextField.textProperty().addListener(getButtonChangeListener());
        addUsernameTextField.setOnKeyPressed(getEnterEventHandler());

        friendsListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> messageBoxController.selectFriend(newValue));
    }

    private EventHandler<KeyEvent> getEnterEventHandler() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER && !addUsernameTextField.getText().trim().isEmpty()) {
                addFriend();
            }
        };
    }

    private void addFriend() {
        if (!addButton.isDisable()) {
            String username = addUsernameTextField.getText().trim();
            friendService.addFriend(Username.fromString(username));

            addUsernameTextField.clear();
            addButton.setDisable(true);

            initFriendsListView();
        }
    }

    private ChangeListener<String> getButtonChangeListener() {
        return (observable, oldValue, newValue) -> {
            String username = addUsernameTextField.getText().trim();

            // TODO some kind of check that a user can't be added twice
            if (!username.isEmpty()) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        };
    }

    private void initFriendsListView() {
        List<Friend> friends = friendService.getAllFriends()
            .distinct()
            .sorted(Comparator.comparing(Friend::getUsername))
            .collect(Collectors.toList());

        observableList.setAll(friends);

        friendsListView.setItems(observableList);
        friendsListView.setCellFactory(listView -> new ListCell<Friend>() {
            @Override
            public void updateItem(Friend friend, boolean empty) {
                super.updateItem(friend, empty);
                if (friend != null) {
                    FriendEntry friendEntry = new FriendEntry(friend);
                    setGraphic(friendEntry.getSelf());
                }
            }
        });
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        addFriend();
    }
}