package ch.hsr.dsa.view.chat.friendsbox;

import ch.hsr.dsa.application.UserService;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.friend.Friend;
import ch.hsr.dsa.view.chat.messagebox.MessageBoxController;
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

    private final UserService userService;

    @FXML
    public TextField addUsernameTextField;

    @FXML
    public Button addButton;

    @FXML
    private ListView<Friend> friendsListView;

    public FriendsBoxController(MessageBoxController messageBoxController, UserService userService) {
        this.messageBoxController = messageBoxController;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        addUsernameTextField.textProperty().addListener(getButtonChangeListener());
        addUsernameTextField.setOnKeyPressed(getEnterEventHandler());

        friendsListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> messageBoxController.selectFriend(newValue));

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
            userService.sendFriendRequest(Username.fromString(username));

            addUsernameTextField.clear();
            addButton.setDisable(true);

            updateFriendsListView();
        }
    }

    public void updateFriendsListView() {
        List<Friend> friends = userService.getAllFriends()
            .sorted(Comparator.comparing(friend -> friend.getFriend().getUsername()))
            .collect(Collectors.toList());

        //TODO same like MessageBoxController
        ObservableList<Friend> observableList = FXCollections.observableArrayList(friends);
        friendsListView.setItems(observableList);
    }

    private ChangeListener<String> getButtonChangeListener() {
        return (observable, oldValue, newValue) -> {
            String username = addUsernameTextField.getText().trim();

            //TODO some kind of check that a user can't be added twice
            if (!username.isEmpty()) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        };
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        addFriend();
    }
}