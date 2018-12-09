package ch.hsr.dcc.application.listener;

import ch.hsr.dcc.application.GroupService;
import ch.hsr.dcc.application.MessageService;
import ch.hsr.dcc.application.UserService;
import ch.hsr.dcc.event.messagereceived.FriendRequestReceivedEvent;
import ch.hsr.dcc.event.messagereceived.GroupAddReceivedEvent;
import ch.hsr.dcc.event.messagereceived.GroupMessageReceivedEvent;
import ch.hsr.dcc.event.messagereceived.MessageReceivedEvent;
import org.springframework.context.event.EventListener;

public class MessageReceivedEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final GroupService groupService;

    public MessageReceivedEventListener(MessageService messageService,
                                        UserService userService,
                                        GroupService groupService) {
        this.messageService = messageService;
        this.userService = userService;
        this.groupService = groupService;
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
    public void friendRequestReceived(FriendRequestReceivedEvent event) {
        userService.friendRequestReceived();
    }

    @EventListener
    public void friendRequestReceived(GroupAddReceivedEvent event) {
        groupService.groupAddReceived();
    }
}
