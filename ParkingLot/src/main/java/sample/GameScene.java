package sample;

import Unit.*;

import Unit.Enum.Direction;
import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameScene {

    private List<GameObject> cars = new ArrayList<>();
    private GameObject selectedObject = null;
    private CarPark carPark = null;
    private JFXButton checkIn;
    private JFXButton checkOut;

    Scene scene;

    private Pane pane;
    private TabPane tabPane;

    GameScene(Parent parent) {

        this.pane = (Pane) parent.lookup("#gamePane");
        this.tabPane = (TabPane) parent.lookup("#tabPane");
        this.checkIn = (JFXButton) parent.lookup("");
        this.checkOut = (JFXButton) parent.lookup("");
        this.scene = parent.getScene();

        if(this.pane == null) {
            throw new Error("Pane must not null");
        }

        if(this.tabPane == null) {
            throw new Error("Tab pane must not null");
        }

        if(this.scene == null) {
            throw new Error("Scene must not null");
        }

        this.tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // set Event Listener with to selected tab
                    if(newValue.getId().compareToIgnoreCase("carSystem") == 0) {
                        setControl();
                    } else if (newValue.getId().compareToIgnoreCase("workSystem") == 0) {
                        removeControl();
                    }
                }
        );

        setControl();

        carPark = new CarPark(pane);
        carPark.generateParkingLot();

    }

    public int getCars() {
        return cars.size();
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public void removeCarPark() {
        this.carPark = null;
    }

    public int getAvailableParkingLots() {
        // get Available Parking Lot from car Park Object
        if(carPark == null) {
            return -1;
        }

        int count = 0;
        for (ParkingLot parkingLot : carPark.getParkingLots()) {

            if (!parkingLot.isParked()) {
                count += 1;
            }
        }

        return count;
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

    // This is a variable
    // Car System Simulator keyboard event Handler
    public EventHandler event = (EventHandler<KeyEvent>) e -> {
        if(e.getCode() == KeyCode.ENTER) {
            spawnCar();
        }

        // ensure there has one car (Testing use)
        if(selectedObject == null) {
            return;
        }

        if(e.getCode() == KeyCode.BACK_SPACE) {
            removeCar(selectedObject);
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
    };

    public void setControl() {
        // Set Control
        scene.setOnKeyPressed(event);
    }

    public void removeControl() {
        // remove eventHandler
        scene.setOnKeyPressed(null);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());
    }

    void spawnCar() {
        System.out.println("Spawn Car");
        Random random = new Random();
        random.setSeed(new Date().getTime());
        Bounds bound = pane.getLayoutBounds();

        Car car = Car.createCar(String.format("A%08d", cars.size() + 1), 4, 40.0);
        Bounds carBound = car.getView().getLayoutBounds();
        addCar(car, random.nextDouble() * 1000 % (bound.getWidth() - carBound.getWidth()), random.nextDouble() * 1000 % (bound.getHeight() - carBound.getHeight()));
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

    private void removeCar(GameObject object) {
        cars.remove(object);
        pane.getChildren().remove(object.getView());
        if(carPark != null) {
            carPark.getParkingLots().forEach(c -> c.setStatus(false));
        }
        selectedObject = null;
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
        if(carPark != null) {
            cars.forEach( car1 -> {
                carPark.getParkingLots().forEach(parkingLot -> {
                    if(parkingLot.isParkedBy(car1)) {
                        return;
                    }
                });
            });
        }

        cars.removeIf(GameObject::isDead);

        // Check Car Collided
        cars.forEach(car -> {
            boolean collide = false;
            for (GameObject car2 : cars) {
                if(car.getMoveSide().compare(car.isColliding(car2))) {
                    collide = true;
                    break;
                }
            }

            // check Car collided with wall and gate
            if(carPark != null) {
                for (Wall wall : carPark.getWalls()) {
                    if(car.getMoveSide().compare(car.isColliding(wall))) {
                        collide = true;
                        break;
                    }
                }

                for (Gate gate : carPark.getGates()) {
                    if(car.getMoveSide().compare(car.isColliding(gate))) {
                        collide = true;
                        break;
                    }
                }
            }

            if(!collide) {
                car.update();
            }
        });

//        cars.forEach(GameObject::update);
    }

}
