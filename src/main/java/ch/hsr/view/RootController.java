package ch.hsr.view;

import ch.hsr.application.UserService;
import ch.hsr.domain.user.Username;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Component
@Getter
public class RootController {

    @FXML
    private HBox loginBox;

    @FXML
    private HBox chatBox;

    public RootController() {

    }
}