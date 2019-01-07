package main;

import Unit.Office;
import config.ConfigManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    VehicleGameScene vehicleGameScene;
    PersonGameScene personGameScene;
    SystemStatus systemStatus;

    Label statusTimeLabel;
    Label carTimeLabel;
    Label workTimeLabel;
    private Label balanceCarPark;
    Label carNumberLbl;

    TabPane tabPaneStatus;

    @Override
    public void start(Stage primaryStage) throws Exception {

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
        statusTimeLabel = (Label) root.lookup("#statusTimeLabel");
        tabPaneStatus = (TabPane) root.lookup("#tabPane");

        tabPaneStatus.getTabs();
        System.out.println(tabPaneStatus.getSelectionModel().getSelectedItem().getText());

        // Create Game Scene
        VehicleGameScene.init(root);
        vehicleGameScene = VehicleGameScene.getInstance();
        vehicleGameScene.startTimer();

        PersonGameScene.init(root);
        personGameScene = PersonGameScene.getInstance();
        personGameScene.startTimer();

        SystemStatus.init(root);
        systemStatus = SystemStatus.getInstance();

        WorkCheckTableViewTab.initialize(root);


        // Schedule update time
        timeInterval(event -> {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            carTimeLabel.setText(formatter.format(localDateTime));
            workTimeLabel.setText(formatter.format(localDateTime));
            statusTimeLabel.setText(formatter.format(localDateTime));
            carNumberLbl.setText(String.format("%d Cars", vehicleGameScene.getCars()));

            //Checking control
            String Status = tabPaneStatus.getSelectionModel().getSelectedItem().getText();
            if(Status.startsWith("Car")){
                personGameScene.removeHumanControl();
                vehicleGameScene.setControl();
            }
            if(Status.startsWith("Work")){
                vehicleGameScene.removeControl();
                personGameScene.setHumanControl();
            }

            int balanceCP = vehicleGameScene.getAvailableParkingLots(); // if no car park will return -1
            if(balanceCP != -1)
                balanceCarPark.setText(String.format("%d Available", balanceCP));
            else
                balanceCarPark.setText("No Available.");

            WorkCheckTableViewTab.getInstance().updateTableView();
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
