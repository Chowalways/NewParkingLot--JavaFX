package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    GameScene gameScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gameScene = new GameScene((Pane) root.lookup("#gamePane"));
        gameScene.setControl(primaryStage);
        gameScene.startTimer();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
