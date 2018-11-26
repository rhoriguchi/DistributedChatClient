package ch.hsr.dsa.event.messagereceived;

import ch.hsr.dsa.application.MessageService;
import ch.hsr.dsa.application.UserService;
import org.springframework.context.event.EventListener;

// TODO once event handling done update view
public class MessageReceiveEventListener {

    private final MessageService messageService;
    private final UserService userService;

    public MessageReceiveEventListener(MessageService messageService,
                                       UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @EventListener
    public void messageReceived(MessageReceivedEvent event) {
        messageService.messageReceived();
    }

    @EventListener
    public void groupMessageReceived(GroupMessageReceivedEvent event) {
        messageService.groupMessageReceived();
    }

    @EventListener
    public void friendRequestReceived(FriendRequestEvent event) {
        userService.friendRequestReceived();
    }
}
