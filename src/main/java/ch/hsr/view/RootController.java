package ch.hsr.view;

import javafx.fxml.FXML;
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