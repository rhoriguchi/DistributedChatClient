package ch.hsr.view.chat.statusbox;

import ch.hsr.application.PeerService;
import ch.hsr.domain.peer.Peer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;


@Controller
public class StatusBoxController {

    private final PeerService peerService;

    private Peer self;

    @FXML
    private Label usernameLabel;

    public StatusBoxController(PeerService peerService) {
        this.peerService = peerService;
    }

    public void updateSelf() {
        self = peerService.getSelf();
        usernameLabel.setText(self.getUsername().toString());
    }
}
