package ch.hsr.dcc.view.listener;

import ch.hsr.dcc.view.chat.friendsbox.FriendGroupBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbSavedEventListenerConfiguration {

    @Bean
    public DbSavedEventListener dbSavedEventListener(FriendGroupBoxController friendGroupBoxController) {
        return new DbSavedEventListener(friendGroupBoxController, messageBoxController);
    }
}
