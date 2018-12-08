package sample;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameScene {

    private List<GameObject> cars = new ArrayList<>();
    private List<CarPark> carParks = new ArrayList<>();
    private GameObject selectedObject = null;
    public final int TOTALCARPARK = 40;
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
        for (CarPark carPark : carParks) {

            if (!carPark.isParked()) {
                count += 1;
            }
        }
        return count;
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
        int x = 0,
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
        carParks.forEach(c -> c.setStatus(false));
        selectedObject = null;
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

        // Check
        cars.forEach(car -> {
            boolean collide = false;
            for (GameObject car2 : cars) {
                if(car.getMoveSide().compare(car.isColliding(car2))) {
                    collide = true;
                }
            }
            if(!collide) {
                car.update();
            }
        });

//        cars.forEach(GameObject::update);
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
            imageView.setFitHeight(20);

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
            else {
                ((Rectangle) getView()).setFill(Color.GREEN);
                car = null;
            }
        }

        public boolean isParked() {
            return car != null;
        }

        public boolean isParkedBy(GameObject other) {

            boolean parked = isColliding(other) == Side.INSIDE;
            System.out.println(other);
            System.out.println(other.isColliding(this));
            if(car == null) {
                if(parked) {
                    setStatus(parked);
                    car = other;
                }
                return parked;
            } else {
                if(isColliding(car) == Side.NONE) {
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
