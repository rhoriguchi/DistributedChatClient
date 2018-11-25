package ch.hsr.event.messagereceived;

import ch.hsr.application.MessageService;
import ch.hsr.application.UserService;
import ch.hsr.view.chat.messagebox.MessageBoxController;
import org.springframework.context.event.EventListener;

public class MessageReceiveEventListener {

    private final MessageBoxController messageBoxController;

    private final MessageService messageService;
    private final UserService userService;

    public MessageReceiveEventListener(MessageBoxController messageBoxController,
                                       MessageService messageService,
                                       UserService userService) {
        this.messageBoxController = messageBoxController;
        this.messageService = messageService;
        this.userService = userService;
    }

    @EventListener
    public void messageReceived(MessageReceivedEvent event) {
        //TODO probably better to do on same layer => controller
        messageService.messageReceived();

        //TODO new listener for frontend
        messageBoxController.updateMessageListView();
    }

    @EventListener
    public void groupMessageReceived(GroupMessageReceivedEvent event) {
        //TODO probably better to do on same layer => controller
        messageService.groupMessageReceived();

        //TODO new listener for frontend
        messageBoxController.updateMessageListView();
    }

    @EventListener
    public void friendRequestReceived(FriendRequestEvent event) {
        //TODO probably better to do on same layer => controller
        userService.friendRequestReceived();

        //TODO trigger something in controller
    }
}
