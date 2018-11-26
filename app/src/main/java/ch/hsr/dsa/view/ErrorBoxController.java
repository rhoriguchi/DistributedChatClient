package ch.hsr.dsa.view;

import ch.hsr.dsa.mapping.peer.PeerMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import java.util.Arrays;
import java.util.stream.Collectors;


@Controller
//TODO use for exceptions
public class ErrorBoxController implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorBoxController.class);

    @FXML
    public Label errorMessage;


    public ErrorBoxController() {
    }

    private void showException(Exception e) {
        errorMessage.setText(e.getMessage());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error(e.getMessage(), e);

        showException((Exception) e);
    }
}