package ch.hsr.dsa.view.chat.messagebox;

import ch.hsr.dsa.application.MessageService;
import ch.hsr.dsa.domain.common.MessageText;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.friend.Friend;
import ch.hsr.dsa.domain.message.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Controller;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageBoxController {

    private final MessageService messageService;

    private Friend other;

    @FXML
    private Label toUsernameLabel;

    @FXML
    private ListView<Message> messageListView;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button sendButton;

    public MessageBoxController(MessageService messageService) {
        this.messageService = messageService;
    }

    @FXML
    private void initialize() {
        messageTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                sendButton.setDisable(true);
            } else {
                sendButton.setDisable(false);
            }
        });

        messageTextArea.setOnKeyPressed(getEnterEventHandler());

        messageListView.setCellFactory(listView -> new ListCell<Message>() {
            @Override
            public void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (message != null) {
                    MessageEntry messageEntry = new MessageEntry(message);
                    setGraphic(messageEntry.getSelf());
                }
            }
        });
    }

    private EventHandler<KeyEvent> getEnterEventHandler() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER && !messageTextArea.getText().trim().isEmpty()) {
                send();
            }
        };
    }

    private void send() {
        if (!sendButton.isDisable()) {
            messageService.sendMessage(
                Username.fromString(toUsernameLabel.getText()),
                MessageText.fromString(messageTextArea.getText().trim())
            );

            messageTextArea.clear();
            sendButton.setDisable(true);

            updateMessageListView(other.getFriend().getUsername());
        }
    }

    public void updateMessageListView(Username otherUsername) {
        List<Message> messages = messageService.getAllMessages(otherUsername)
            .sorted(Comparator.comparing(Message::getTimeStamp))
            .collect(Collectors.toList());

        //TODO fucked up, when larger list gets loaded and less item come after it will show all that are more
        ObservableList<Message> observableList = FXCollections.observableArrayList(messages);
        messageListView.setItems(observableList);
    }

    @FXML
    private void send(ActionEvent event) {
        send();
    }

    public void selectFriend(Friend friend) {
        other = friend;

        toUsernameLabel.setText(other.getFriend().getUsername().toString());
        updateMessageListView(other.getFriend().getUsername());
    }

    public void updateMessageListView() {
        updateMessageListView(other.getFriend().getUsername());
    }
}
