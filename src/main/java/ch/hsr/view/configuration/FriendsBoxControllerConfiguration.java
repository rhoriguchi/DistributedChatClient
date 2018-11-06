package ch.hsr.view.configuration;

import ch.hsr.application.FriendService;
import ch.hsr.view.chat.friendsbox.FriendsBoxController;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendsBoxControllerConfiguration {

    @Bean
    public FriendsBoxController friendsBoxController(MessageBoxController messageBoxController,
                                                     FriendService friendService) {
        return new FriendsBoxController(messageBoxController, friendService);
    }
}
