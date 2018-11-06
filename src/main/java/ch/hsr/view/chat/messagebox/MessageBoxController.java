package ch.hsr.view.chat.messagebox;

import ch.hsr.application.MessageService;
import ch.hsr.domain.common.PeerId;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageText;
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
    private TextArea sendTextArea;

    @FXML
    private Button sendButton;

    public MessageBoxController(MessageService messageService) {
        this.messageService = messageService;
    }

    @FXML
    private void initialize() {
        sendTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                sendButton.setDisable(true);
            } else {
                sendButton.setDisable(false);
            }
        });

        sendTextArea.setOnKeyPressed(getEnterEventHandler());

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
            if (event.getCode() == KeyCode.ENTER && !sendTextArea.getText().trim().isEmpty()) {
                send();
            }
        };
    }

    private void send() {
        if (!sendButton.isDisable()) {
            messageService.send(
                Username.fromString(toUsernameLabel.getText()),
                MessageText.fromString(sendTextArea.getText().trim())
            );

            sendTextArea.clear();
            sendButton.setDisable(true);

            updateMessageListView(other.getPeerId());
        }
    }

    @FXML
    private void send(ActionEvent event) {
        send();
    }

    public void selectFriend(Friend friend) {
        other = friend;

        toUsernameLabel.setText(other.getUsername().toString());
        updateMessageListView(other.getPeerId());
    }

    public void updateMessageListView(PeerId otherId) {
        List<Message> messages = messageService.getAllMessages(otherId)
            .sorted(Comparator.comparing(Message::getMessageTimeStamp))
            .collect(Collectors.toList());

        // TODO fucked up, when larger list gets loaded and less item come after it will show all that are more
        ObservableList<Message> observableList = FXCollections.observableArrayList(messages);
        messageListView.setItems(observableList);
    }
}
