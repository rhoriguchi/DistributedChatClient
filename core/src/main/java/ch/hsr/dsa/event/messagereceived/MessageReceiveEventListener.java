package ch.hsr.dsa.event.messagereceived;

import ch.hsr.dsa.application.MessageService;
import ch.hsr.dsa.application.UserService;
import org.springframework.context.event.EventListener;

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
        //TODO probably better to do on same layer => controller
        messageService.messageReceived();
    }

    @EventListener
    public void groupMessageReceived(GroupMessageReceivedEvent event) {
        //TODO probably better to do on same layer => controller
        messageService.groupMessageReceived();
    }

    @EventListener
    public void friendRequestReceived(FriendRequestEvent event) {
        //TODO probably better to do on same layer => controller
        userService.friendRequestReceived();

        //TODO trigger something in controller
    }
}
