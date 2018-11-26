package ch.hsr.dsa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.IOException;


//TODO logger config https://dzone.com/articles/configuring-logback-with-spring-boot
//TODO check all catch blocks, that exceptions get logged and right exception type, maybe make custom type
//TODO handle all dbObjects that hava failed true
//TODO javaFx themes https://stackoverflow.com/questions/28474914/javafx-css-themes
@SpringBootApplication
public class ChatClient extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent root;

    public static void main(String[] args) {
        Application.launch(ChatClient.class, args);
    }

    @Override
    public void init() throws IOException {
        springContext = SpringApplication.run(ChatClient.class);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/root.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Chat client");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void stop() {
        // TODO call PeerService.logout()
        springContext.stop();
    }
}
