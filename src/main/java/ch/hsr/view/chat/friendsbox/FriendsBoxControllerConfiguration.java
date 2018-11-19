package ch.hsr.view.chat.friendsbox;

import ch.hsr.application.UserService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendsBoxControllerConfiguration {

    @Bean
    public FriendsBoxController friendsBoxController(MessageBoxController messageBoxController,
                                                     UserService userService) {
        return new FriendsBoxController(messageBoxController, userService);
    }
}
