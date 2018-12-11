package ch.hsr.dcc.view.chat.friendsbox;

import ch.hsr.dcc.application.UserService;
import ch.hsr.dcc.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendsBoxControllerConfiguration {

    @Bean
    public FriendGroupBoxController friendGroupBoxController(MessageBoxController messageBoxController,
                                                         UserService userService) {
        return new FriendGroupBoxController(messageBoxController, userService);
    }
}
