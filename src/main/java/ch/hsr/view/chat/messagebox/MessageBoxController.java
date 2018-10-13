package ch.hsr.view.chat.messagebox;

import ch.hsr.application.MessageService;
import ch.hsr.application.UserService;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageText;
import ch.hsr.domain.user.Username;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MessageBoxController {

    private final MessageService messageService;
    private final UserService userService;

    @FXML
    // TODO set this when click on peer list
    private Label usernameLabel;

    @FXML
    private ListView<Message> messageListView;
    private ObservableList<Message> observableList = FXCollections.observableArrayList();

    @FXML
    private TextField sendTextField;

    @FXML
    private Button sendButton;

    public MessageBoxController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @FXML
    protected void initialize() {
        sendTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                sendButton.setDisable(true);
            } else {
                sendButton.setDisable(false);
            }
        });

        sendTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
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

    @FXML
    private void send(ActionEvent event) {
        send();
    }

    private void send() {
        String messageText = sendTextField.getText();
        if (!messageText.isEmpty()) {
            Message message = new Message(
                userService.getSelf(),
                Username.fromString(usernameLabel.getText()),
                MessageText.fromString(messageText)
            );

            messageService.send(message);

            observableList.add(message);
            messageListView.setItems(observableList);

            sendTextField.setText("");
        }
    }
}
