package ch.hsr.dsa.application.listener;

import ch.hsr.dsa.application.MessageService;
import ch.hsr.dsa.application.UserService;
import ch.hsr.dsa.event.messagereceived.FriendRequestEvent;
import ch.hsr.dsa.event.messagereceived.GroupMessageReceivedEvent;
import ch.hsr.dsa.event.messagereceived.MessageReceivedEvent;
import org.springframework.context.event.EventListener;

public class MessageReceivedEventListener {

    private final MessageService messageService;
    private final UserService userService;

    public MessageReceivedEventListener(MessageService messageService,
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
