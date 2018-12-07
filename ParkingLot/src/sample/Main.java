package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    GameScene gameScene;

    Label lblTIme;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("res/css/main.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        lblTIme = (Label) root.lookup("#lblTime");

        // Create Game Scene
        gameScene = new GameScene((Pane) root.lookup("#gamePane"));
        gameScene.setControl(primaryStage);
        gameScene.startTimer();


        // Schedule update time
        timeInterval(event -> {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC+08:00"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            lblTIme.setText(formatter.format(localDateTime));
        }, 1);


        System.out.println(getClass().getResource("res/css/main.css"));

    }

    private void timeInterval(EventHandler event, long secondDuration) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(secondDuration),
                event));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
