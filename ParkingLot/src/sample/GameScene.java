package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameScene extends Application {

    List<GameObject> cars = new ArrayList<>();
    GameObject selectedObject = null;

    @FXML
    private Pane pane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load fxml Scene from url
        Parent root = FXMLLoader.load(getClass().getResource("gameScene.fxml"));
        primaryStage.setTitle("Check In System");
        Scene scene = new Scene(root);

        // Update scene timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };

        timer.start();

        primaryStage.setScene(scene);
        setControl(primaryStage);

        // find pane from root
        pane = (Pane) root.lookup("#pane");

        // show the stage
        primaryStage.show();

        // create car if there is not exists (testing use)
        if(cars.size() == 0) {
            Car car = Car.createCar();

            addCar(car, 300, 300);
            car = Car.createCar();
            addCar(car, 100, 200);
        }
    }

    public void setControl(Stage stage) {
        stage.getScene().setOnKeyPressed(e -> {
            // ensure there has one car (Testing use)
            if(selectedObject == null) {
                return;
            }

            if(e.getCode() == KeyCode.DOWN) {
                selectedObject.toggleReverse();
            }

            if(e.getCode() == KeyCode.UP) {
                selectedObject.toggleStop();
            }

            if(e.getCode() == KeyCode.LEFT) {
                selectedObject.rotateLeft();
            }

            if(e.getCode() == KeyCode.RIGHT) {
                selectedObject.rotateRight();
            }
        });
    }

    private void addCar(GameObject object, double x, double y) {
        object.addEventFilter(MouseEvent.MOUSE_CLICKED,
                e -> {
                    System.out.println("Click");
                    selectedObject = object;
//                    System.out.println(object instanceof Car);
                });
        cars.add(object);
        addGameObject(object, x, y);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());

    }

    private void onUpdate() {
        cars.forEach(GameObject::update);
    }

    public static void main(String[] args) {
        launch(args);
    }


    static class Car extends GameObject {


        public Car(ImageView image) {
            super(image);
        }

        public static Car createCar() {
            ImageView imageView = new ImageView();
            Image image = new Image("res/images/car.png");
            imageView.setImage(image);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);

            return new Car(imageView);
        }

    }

}
