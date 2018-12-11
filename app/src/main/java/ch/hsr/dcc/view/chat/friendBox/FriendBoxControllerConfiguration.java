package ch.hsr.dcc.view.chat.friendBox;

import ch.hsr.dcc.application.UserService;
import ch.hsr.dcc.view.chat.messagebox.MessageBoxController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendBoxControllerConfiguration {

    @Bean
    public FriendBoxController friendBoxController(MessageBoxController messageBoxController,
                                                        UserService userService) {
        return new FriendBoxController(messageBoxController, userService);
    }
}
