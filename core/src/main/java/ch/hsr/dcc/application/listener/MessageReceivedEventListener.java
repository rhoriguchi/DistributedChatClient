package ch.hsr.dcc.application.listener;

import ch.hsr.dcc.application.MessageService;
import ch.hsr.dcc.application.UserService;
import ch.hsr.dcc.event.messagereceived.FriendRequestEvent;
import ch.hsr.dcc.event.messagereceived.GroupMessageReceivedEvent;
import ch.hsr.dcc.event.messagereceived.MessageReceivedEvent;
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
