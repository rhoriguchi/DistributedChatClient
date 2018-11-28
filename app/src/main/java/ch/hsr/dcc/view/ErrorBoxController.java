package ch.hsr.dcc.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;


@Controller
public class ErrorBoxController implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorBoxController.class);

    @FXML
    public Label errorMessage;


    public ErrorBoxController() {
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error(e.getMessage(), e);
        showException((Exception) e);
    }

    private void showException(Exception e) {
        errorMessage.setText(e.getMessage());
    }
}