package ch.hsr.dcc.view.listener;

import ch.hsr.dcc.event.dbchanged.DbFriendSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbGroupMessageSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbGroupSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbMessageSavedEvent;
import ch.hsr.dcc.view.chat.friendBox.FriendBoxController;
import ch.hsr.dcc.view.chat.messagebox.MessageBoxController;
import javafx.application.Platform;
import org.springframework.context.event.EventListener;

//TODO use Platform.runLater()
public class DbSavedEventListener {

    private final FriendBoxController friendBoxController;
    private final MessageBoxController messageBoxController;

    public DbSavedEventListener(FriendBoxController friendBoxController,
                                MessageBoxController messageBoxController) {
        this.friendBoxController = friendBoxController;
        this.messageBoxController = messageBoxController;
    }

    @EventListener
    public void dbFriendSaved(DbFriendSavedEvent dbFriendSavedEvent) {
        Platform.runLater(friendBoxController::updateFriendsListView);
    }

    @EventListener
    public void dbGroupSaved(DbGroupSavedEvent dbGroupSavedEvent) {
        Long id = dbGroupSavedEvent.getId();

        //TODO implement
    }

    @EventListener
    public void dbMessageSaved(DbMessageSavedEvent dbMessageSavedEvent) {
        Long id = dbMessageSavedEvent.getId();
        Platform.runLater(() -> messageBoxController.messageReceived(id));
    }

    @EventListener
    public void dbDbGroupMessageSaved(DbGroupMessageSavedEvent dbGroupMessageSavedEvent) {
        Long id = dbGroupMessageSavedEvent.getId();

        //TODO implement
    }
}
