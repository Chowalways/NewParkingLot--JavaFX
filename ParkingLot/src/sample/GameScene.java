package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameScene {

    List<GameObject> cars = new ArrayList<>();
    GameObject selectedObject = null;

    @FXML
    private Pane pane;

    GameScene(Pane pane) {
        if(pane == null) {
            throw new Error("Pane must not null");
        }
        this.pane = pane;
    }

    void spawnCar() {

        Random random = new Random();
        random.setSeed(new Date().getTime());
        Bounds bound = pane.getLayoutBounds();

        Car car = Car.createCar();
        Bounds carBound = car.getView().getLayoutBounds();
        System.out.println(bound.getWidth());
        System.out.println(carBound.getWidth());
        addCar(car, random.nextDouble() * 1000 % (bound.getWidth() - carBound.getWidth()), random.nextDouble() * 1000 % (bound.getHeight() - carBound.getHeight()));
    }


    void startTimer() {
        // Update scene timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        // load fxml Scene from url
//        Parent root = FXMLLoader.load(getClass().getResource("gameScene.fxml"));
//        primaryStage.setTitle("Check In System");
//        Scene scene = new Scene(root);
//
//
//
//
//        primaryStage.setScene(scene);
//
//        // set Scene
//        setControl(primaryStage);
//
//        // find pane from root
//        pane = (Pane) root.lookup("#pane");
//
//        // show the stage
//        primaryStage.show();
//
//        // create car if there is not exists (testing use)
//        if(cars.size() == 0) {
//            Car car = Car.createCar();
//
//            addCar(car, 300, 300);
//            car = Car.createCar();
//            addCar(car, 100, 200);
//        }
//    }

    public void setControl(Stage stage) {

        if(stage.getScene() == null) {
            throw new Error("Scene must not null");
        }

        System.out.println("Try Control");

        stage.getScene().setOnKeyPressed(e -> {
            System.out.println("Try Control");
            System.out.println(e.getCode());

            if(e.getCode() == KeyCode.ENTER) {
                spawnCar();
            }

            // ensure there has one car (Testing use)
            if(selectedObject == null) {
                return;
            }

            if(e.getCode() == KeyCode.S) {
                selectedObject.toggleReverse();
            }

            if(e.getCode() == KeyCode.W) {
                selectedObject.toggleStop();
            }

            if(e.getCode() == KeyCode.A) {
                selectedObject.rotateLeft();
            }

            if(e.getCode() == KeyCode.D) {
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
        cars.forEach( car1 -> {
            cars.forEach( car2 -> {
                if(car1.isColliding(car2)) {
                    GameObject deadCar;
                    if(car1 == selectedObject)
                        deadCar = car2;
                    else
                        deadCar = car1;
                    deadCar.setAlive(false);
                    pane.getChildren().remove(deadCar.getView());
                    return;
                }
            });
        });

        cars.removeIf(GameObject::isDead);

        cars.forEach(GameObject::update);
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
