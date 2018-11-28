package ch.hsr.dcc.view.listener;

import ch.hsr.dcc.event.dbchanged.DbFriendSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbGroupMessageSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbGroupSavedEvent;
import ch.hsr.dcc.event.dbchanged.DbMessageSavedEvent;
import org.springframework.context.event.EventListener;

//TODO use Platform.runLater()
public class DbSavedEventListener {

    public DbSavedEventListener() {

    }

    @EventListener
    public void dbFriendSaved(DbFriendSavedEvent dbFriendSavedEvent) {
        String username = dbFriendSavedEvent.getUsername();

        //TODO implement
    }

    @EventListener
    public void dbGroupSaved(DbGroupSavedEvent dbGroupSavedEvent) {
        Long id = dbGroupSavedEvent.getId();

        //TODO implement
    }

    @EventListener
    public void dbMessageSaved(DbMessageSavedEvent dbMessageSavedEvent) {
        Long id = dbMessageSavedEvent.getId();

        //TODO implement
    }

    @EventListener
    public void dbDbGroupMessageSaved(DbGroupMessageSavedEvent dbGroupMessageSavedEvent) {
        Long id = dbGroupMessageSavedEvent.getId();

        //TODO implement
    }
}
