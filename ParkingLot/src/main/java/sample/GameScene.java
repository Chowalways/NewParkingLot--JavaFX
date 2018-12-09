package sample;

import Unit.Gate;
import Unit.ParkingLot;
import Unit.Car;

import Unit.Enum.Direction;
import Unit.Wall;
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
    private List<ParkingLot> parkingLots = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private List<Gate> gates = new ArrayList<>();
    private GameObject selectedObject = null;
    public final int TOTALPARKINGLOT = 20;
    Scene scene;

    private Pane pane;
    private TabPane tabPane;

    GameScene(Parent parent) {

        this.pane = (Pane) parent.lookup("#gamePane");
        this.tabPane = (TabPane) parent.lookup("#tabPane");
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
                    if(newValue.getId().compareToIgnoreCase("carSystem") == 0) {
                        setControl();
                    } else if (newValue.getId().compareToIgnoreCase("workSystem") == 0) {
                        removeControl();
                    }
                }
        );

        setControl();

        initialPath();
    }

    public int getCars() {
        return cars.size();
    }

    public int getAvailableCarPark() {
        int count = 0;
        for (ParkingLot parkingLot : parkingLots) {

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

    void initialPath() {

        System.out.println("Spawn Car Park");
        int ParkingLotPadding = 20,
                x = ParkingLotPadding,
            y = ParkingLotPadding,
            carPerColumn = 5,
            xSpacing = 50,
            ySpacing = 10;

        for (int i = 0; i < TOTALPARKINGLOT; i++) {
            if (i != 0 && i % carPerColumn == 0) {
                y = ParkingLotPadding;
                x += ParkingLot.WIDTH + xSpacing;
            }
            spawnParkingLot(x, y);
            y += ParkingLot.HEIGHT + ySpacing;
        }


        int wallPadding = ParkingLotPadding - 10,
                parkingLotWidth = x + ParkingLot.WIDTH + 60,
            parkingLotHeight = y + 200;
        // Generate TOP Wall
        for (int i = 0; i * Wall.LONG < parkingLotWidth; i ++ ) {
            spawnHorizontalWall(i * Wall.LONG + wallPadding, 0 + wallPadding);
        }

        // Generate Bottom Wall
        for (int i = 0; i * Wall.LONG < parkingLotWidth; i ++ ) {
            spawnHorizontalWall(i * Wall.LONG + wallPadding, parkingLotHeight + wallPadding);
        }

        // Generate LEFT Wall
        for (int i = 0; i * Wall.LONG < parkingLotHeight; i ++ ) {
            spawnVerticalWall(0 + wallPadding, i * Wall.LONG + wallPadding);
        }

        // Generate RIGHT Wall
        for (int i = 0; i * Wall.LONG < parkingLotHeight; i ++ ) {
            spawnVerticalWall(parkingLotWidth + wallPadding + 10, i * Wall.LONG + wallPadding);
        }

        // generate Gate
        spawnHorizontalGate(55, parkingLotHeight + wallPadding - 10);

    }

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
        scene.setOnKeyPressed(event);
    }

    public void removeControl() {
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

        Car car = Car.createCar();
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
        parkingLots.forEach(c -> c.setStatus(false));
        selectedObject = null;
    }

    void spawnParkingLot(int x, int y) {
        ParkingLot parkingLot = ParkingLot.createCarPark();
        addParkingLot(parkingLot, x, y);
    }

    private void addParkingLot(ParkingLot object, double x, double y) {
        parkingLots.add(object);
        addGameObject(object, x, y);
    }

    void spawnHorizontalWall(int x, int y) {
        Wall wall = Wall.creteWall(Direction.HORIZONTAL);
        addWall(wall, x, y);
    }

    void spawnVerticalWall(int x, int y) {
        Wall wall = Wall.creteWall(Direction.VERTICAL);
        addWall(wall, x, y);
    }

    private void addWall(Wall wall, int x, int y) {
        walls.add(wall);
        addGameObject(wall, x, y);
    }

    void spawnHorizontalGate(int x, int y) {
        Gate gate = Gate.createGate(Direction.HORIZONTAL);
        addGate(gate, x, y);
    }

    void spawnVerticalGate(int x, int y) {
        Gate gate = Gate.createGate(Direction.VERTICAL);
        addGate(gate, x, y);
    }

    private void addGate(Gate gate, int x, int y) {
        gates.add(gate);
        addGameObject(gate, x, y);
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
            parkingLots.forEach(parkingLot -> {
                if(parkingLot.isParkedBy(car1)) {
                    return;
                }
            });
        });

        cars.removeIf(GameObject::isDead);

        // Check
        cars.forEach(car -> {
            boolean collide = false;
            for (GameObject car2 : cars) {
                if(car.getMoveSide().compare(car.isColliding(car2))) {
                    collide = true;
                    break;
                }
            }

            for (Wall wall : walls) {
                if(car.getMoveSide().compare(car.isColliding(wall))) {
                    collide = true;
                    break;
                }
            }

            for (Gate gate : gates) {
                if(car.getMoveSide().compare(car.isColliding(gate))) {
                    collide = true;
                    break;
                }
            }

            if(!collide) {
                car.update();
            }
        });

        // remove wall when collided with gate
        for (Wall wall : walls) {
            for (Gate gate : gates) {
                if(gate.isColliding(wall) == Side.INSIDE) {
                    wall.setAlive(false);
                    pane.getChildren().remove(wall.getView());
                }
            }
        }
        walls.removeIf(GameObject::isDead);

//        cars.forEach(GameObject::update);
    }

}
