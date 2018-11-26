package ch.hsr.dsa.view.chat.friendsbox;

import ch.hsr.dsa.application.UserService;
import ch.hsr.dsa.view.chat.messagebox.MessageBoxController;
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
