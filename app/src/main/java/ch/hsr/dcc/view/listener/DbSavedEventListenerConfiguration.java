package ch.hsr.dcc.view.listener;

import ch.hsr.dcc.view.chat.friendBox.FriendBoxController;
import ch.hsr.dcc.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbSavedEventListenerConfiguration {

    @Bean
    public DbSavedEventListener dbSavedEventListener(FriendBoxController friendBoxController,
                                                     MessageBoxController messageBoxController) {
        return new DbSavedEventListener(friendBoxController, messageBoxController);
    }
}
