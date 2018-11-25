package ch.hsr.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import java.util.Arrays;
import java.util.stream.Collectors;


@Controller
//TODO use for exceptions
public class ErrorBoxController {

    @FXML
    public Label errorMessage;

    @FXML
    public Label stackTrace;


    public ErrorBoxController() {
    }

    public void showException(Exception e) {
        errorMessage.setText(e.getMessage());
        stackTrace.setText(Arrays.stream(e.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n")));
    }
}