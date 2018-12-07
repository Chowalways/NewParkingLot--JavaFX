package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameScene {

    private List<GameObject> cars = new ArrayList<>();
    private List<CarPark> carParks = new ArrayList<>();
    private GameObject selectedObject = null;

    private Pane pane;

    GameScene(Pane pane, Label carNumberLbl) {
        if(pane == null) {
            throw new Error("Pane must not null");
        }

        this.pane = pane;
        initialPath();
    }

    public int getCars() {
        return cars.size();
    }

    void spawnCar() {
        System.out.println("Spawn Car");
        Random random = new Random();
        random.setSeed(new Date().getTime());
        Bounds bound = pane.getLayoutBounds();

        Car car = Car.createCar();
        Bounds carBound = car.getView().getLayoutBounds();
        addCar(car, random.nextDouble() * 1000 % (bound.getWidth() - carBound.getWidth()), random.nextDouble() * 1000 % (bound.getHeight() - carBound.getHeight()));
    }

    void spawnCarPark(int x, int y) {
        CarPark carPark = CarPark.createCarPark();
        addCarPark(carPark, x, y);
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

    void initialPath() {

        System.out.println("Spawn Car Park");
        int TOTALCARPARK = 40,
                x = 0,
                y = 0,
                carPerColumn = 5,
                xSpacing = 50,
                ySpacing = 10;
        for (int i = 0; i < TOTALCARPARK; i++) {
            if (i != 0 && i % carPerColumn == 0) {
                y = 0;
                x += CarPark.WIDTH + xSpacing;
            }
            spawnCarPark(x, y);
            y += CarPark.HEIGHT + ySpacing;
        }
    }

    public void setControl(Stage stage) {

        if(stage.getScene() == null) {
            throw new Error("Scene must not null");
        }

        stage.getScene().setOnKeyPressed(e -> {
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

        object
            .addEventFilter(MouseEvent.MOUSE_CLICKED,
            e -> {
                selectedObject = object;
            });

        cars.add(object);
        addGameObject(object, x, y);
    }

    private void addCarPark(CarPark object, double x, double y) {
        carParks.add(object);
        addGameObject(object, x, y);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());
    }

    private void onUpdate() {

        // destroyed car mode
//        cars.forEach( car1 -> {
//            cars.forEach( car2 -> {
//                if(car1.isColliding(car2)) {
//                    GameObject deadCar;
//                    if(car1 == selectedObject)
//                        deadCar = car2;
//                    else
//                        deadCar = car1;
//                    deadCar.setAlive(false);
//                    pane.getChildren().remove(deadCar.getView());
//                    return;
//                }
//            });
//        });


        // car parking mode
        cars.forEach( car1 -> {
            carParks.forEach(carPark -> {
                if(carPark.isParkedBy(car1)) {
                    return;
                }
            });
        });

        cars.removeIf(GameObject::isDead);

        cars.forEach(GameObject::update);
    }

    static class Car extends GameObject {

        private Car(ImageView image) {
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

    static class CarPark extends GameObject {

        GameObject car;
        public static final int WIDTH = 30;
        public static final int HEIGHT = 30;


        private CarPark(Rectangle rectangle) {
            super(rectangle);
        }

        public static CarPark createCarPark() {
            Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);

            rectangle.setFill(Color.GREEN);
            rectangle.setStrokeWidth(2);
            rectangle.setStroke(Color.GRAY);

            return new CarPark(rectangle);
        }

        private void setStatus(boolean status) {
            if(status)
                ((Rectangle) getView()).setFill(Color.RED);
            else
                ((Rectangle) getView()).setFill(Color.GREEN);
        }

        public boolean isParkedBy(GameObject other) {
            boolean parked = this.isColliding(other);
            if(car == null) {
                if(parked) {
                    setStatus(parked);
                    car = other;
                }
                return parked;
            } else {
                if(!this.isColliding(car)) {
                    setStatus(false);
                    car = null;
                    return false;
                } else {
                    return true;
                }
            }

        }
    }

}
