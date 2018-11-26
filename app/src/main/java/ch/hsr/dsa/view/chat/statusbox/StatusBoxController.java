package ch.hsr.dsa.view.chat.statusbox;

import ch.hsr.dsa.application.PeerService;
import ch.hsr.dsa.domain.peer.Peer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;


@Controller
public class StatusBoxController {

    private final PeerService peerService;

    @FXML
    private Label selfLabel;

    public StatusBoxController(PeerService peerService) {
        this.peerService = peerService;
    }

    public void updateSelf() {
        Peer self = peerService.getSelf();
        selfLabel.setText(String.format("%s (%s)", self.getUsername(), self.getIpAddress()));
    }
}
