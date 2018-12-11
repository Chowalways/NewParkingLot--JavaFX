package main;

import config.ConfigManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    GameScene gameScene;

    Label carTimeLabel;
    Label workTimeLabel;
    private Label balanceCarPark;
    Label carNumberLbl;

    @Override
    public void start(Stage primaryStage) throws Exception{

        ConfigManager.init("config");
        Parent root = FXMLLoader.load(getClass().getResource("gameScene.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(ClassLoader.getSystemResource("css/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        carTimeLabel = (Label) root.lookup("#carTimeLabel");
        workTimeLabel = (Label) root.lookup("#workTimeLabel");
        carNumberLbl = (Label) root.lookup("#carNumberLbl");
        balanceCarPark = (Label) root.lookup("#balanceLabel");

        // Create Game Scene
        GameScene.init(root);
        gameScene = GameScene.getInstance();
        gameScene.startTimer();

        // Schedule update time
        timeInterval(event -> {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            carTimeLabel.setText(formatter.format(localDateTime));
            workTimeLabel.setText(formatter.format(localDateTime));
            carNumberLbl.setText(String.format("%d Cars", gameScene.getCars()));

            int balanceCP = gameScene.getAvailableParkingLots(); // if no car park will return -1
            if(balanceCP != -1)
                balanceCarPark.setText(String.format("%d Available", balanceCP));
            else
                balanceCarPark.setText("No Available.");
        }, 1);

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
