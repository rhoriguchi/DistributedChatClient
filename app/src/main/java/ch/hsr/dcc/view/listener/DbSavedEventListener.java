package ch.hsr.dcc.view.listener;

import ch.hsr.dcc.event.dbchanged.DbFriendSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbGroupMessageSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbGroupSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbMessageSavedEvent;
import ch.hsr.dcc.view.chat.friendsbox.FriendGroupBoxController;
import ch.hsr.dcc.view.chat.messagebox.MessageBoxController;
import org.springframework.context.event.EventListener;

//TODO use Platform.runLater()
public class DbSavedEventListener {

    private final FriendGroupBoxController FriendGroupBoxController;
    private final MessageBoxController MessageBoxController;

    public DbSavedEventListener(ch.hsr.dcc.view.chat.friendsbox.FriendGroupBoxController friendGroupBoxController, ch.hsr.dcc.view.chat.messagebox.MessageBoxController messageBoxController) {

        FriendGroupBoxController = friendGroupBoxController;
        MessageBoxController = messageBoxController;
    }

    @EventListener
    public void dbFriendSaved(DbFriendSavedEvent dbFriendSavedEvent) {
        FriendGroupBoxController.updateFriendsListView();
    }

    @EventListener
    public void dbGroupSaved(DbGroupSavedEvent dbGroupSavedEvent) {
        Long id = dbGroupSavedEvent.getId();

        //TODO implement
    }

    @EventListener
    public void dbMessageSaved(DbMessageSavedEvent dbMessageSavedEvent) {
        Long id = dbMessageSavedEvent.getId();
        MessageBoxController.messageReceived(id);
    }

    @EventListener
    public void dbDbGroupMessageSaved(DbGroupMessageSavedEvent dbGroupMessageSavedEvent) {
        Long id = dbGroupMessageSavedEvent.getId();

        //TODO implement
    }
}
