package ch.hsr.view.chat.messagebox;

import ch.hsr.application.MessageService;
import ch.hsr.application.PeerService;
import ch.hsr.domain.friend.Friend;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageText;
import ch.hsr.domain.peer.Peer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.springframework.stereotype.Controller;

@Controller
public class MessageBoxController {

    private final MessageService messageService;
    private final PeerService peerService;

    private Peer toPeer;

    @FXML
    private Label toUsernameLabel;

    @FXML
    private ListView<Message> messageListView;
    private ObservableList<Message> observableList = FXCollections.observableArrayList();

    @FXML
    private TextArea sendTextArea;

    @FXML
    private Button sendButton;

    public MessageBoxController(MessageService messageService, PeerService peerService) {
        this.messageService = messageService;
        this.peerService = peerService;
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

        sendTextArea.setOnKeyReleased(event -> {
            if (new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN).match(event)) {
                sendTextArea.insertText(sendTextArea.getCaretPosition(), "\n");
            } else if (event.getCode() == KeyCode.ENTER && !sendTextArea.getText().trim().isEmpty()) {
                send();
            }
        });

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

    private void send() {
        if (!sendButton.isDisable()) {
            Message message = new Message(
                peerService.getSelf(),
                toPeer,
                MessageText.fromString(sendTextArea.getText().trim())
            );

            messageService.send(message);

            observableList.add(message);
            messageListView.setItems(observableList);

            sendTextArea.clear();
            sendButton.setDisable(true);
        }
    }

    @FXML
    private void send(ActionEvent event) {
        send();
    }

    public void selectFriend(Friend friend) {
        toUsernameLabel.setText(friend.getUsername().toString());

        // TODO needs logic to save new user
        // TODO needs logic to load old messages and remove current messages, maybe use some kind of map or so, so that messages don't have to be reloaded or at least use a cash
    }
}
