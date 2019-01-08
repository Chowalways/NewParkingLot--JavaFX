package main;

import com.sun.javafx.scene.control.skin.LabeledText;
import config.ConfigManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    VehicleGameScene vehicleGameScene;
    PersonGameScene personGameScene;
    WorkCheckTableViewTab workCheckTableViewTab;
    CarTicketTableViewTab carTicketTableViewTab;

    Label carTimeLabel;
    Label workTimeLabel;
    private Label balanceCarPark;
    Label carNumberLbl;

    TabPane tabPaneStatus;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initialize(primaryStage);

        tabPaneStatus.addEventFilter(MouseEvent.MOUSE_CLICKED, onTabChange);

        // Schedule update time
        timeInterval(event -> {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            carTimeLabel.setText(formatter.format(localDateTime));
            workTimeLabel.setText(formatter.format(localDateTime));
            carNumberLbl.setText(String.format("%d Cars", vehicleGameScene.getCars()));

            int balanceCP = vehicleGameScene.getAvailableParkingLots(); // if no car park will return -1
            if(balanceCP != -1)
                balanceCarPark.setText(String.format("%d Available", balanceCP));
            else
                balanceCarPark.setText("No Available.");

            WorkCheckTableViewTab.getInstance().updateTableView();
        }, 1);
    }
    public void initialize(Stage primaryStage) throws Exception {

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
        tabPaneStatus = (TabPane) root.lookup("#tabPane");

        tabPaneStatus.getTabs();
        System.out.println(tabPaneStatus.getSelectionModel().getSelectedItem().getText());

        // Create Game Scene
        VehicleGameScene.init(root);
        vehicleGameScene = VehicleGameScene.getInstance();
        vehicleGameScene.setControl();
        vehicleGameScene.startTimer();

        PersonGameScene.init(root);
        personGameScene = PersonGameScene.getInstance();
        personGameScene.startTimer();

        WorkCheckTableViewTab.init(root);
        workCheckTableViewTab = WorkCheckTableViewTab.getInstance();

        CarTicketTableViewTab.init(root);
        carTicketTableViewTab = CarTicketTableViewTab.getInstance();
    }

    private EventHandler onTabChange = e -> {
        EventTarget events = e.getTarget();
        if(events instanceof  LabeledText) {
            String text = ((LabeledText)events).getText();
            System.out.println(((LabeledText)events).getText());

            personGameScene.removeHumanControl();
            vehicleGameScene.removeControl();

            if(text.equalsIgnoreCase("Car System Simulator")){
                vehicleGameScene.setControl();
            }
            else if(text.equalsIgnoreCase("Work System Simulator")){
                personGameScene.setHumanControl();
            }
            else if(text.equalsIgnoreCase("Work Check History")){
                workCheckTableViewTab.updateTableView();
            } else if(text.equalsIgnoreCase("Car Tickets")) {
                carTicketTableViewTab.updateTableView();
            }
        }
    };

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
