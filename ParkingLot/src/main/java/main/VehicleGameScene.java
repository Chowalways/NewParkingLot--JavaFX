package main;

import Abstract.CheckInTicket;
import Abstract.GameObject;
import CheckSystem.Other.CheckInStatus;
import Unit.*;

import Unit.Enum.Side;
import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class VehicleGameScene {
    private List<GameObject> cars = new ArrayList<>();
    private Car selectedCar = null;

    // Payment System
    private PaymentMachine paymentMachine = null;
    private Gate gate = null;

    private CarPark carPark = null;

    private Label ticketIDLabel;
    private Label ticketTimeInLabel;
    private Label ticketPayTimeLabel;
    private Label ticketPriceLabel;

    private Label carStatusLabel;

    private JFXButton checkIn;
    private JFXButton checkOut;
    private JFXButton insertCard;
    private JFXButton pay;

    Scene scene;

    private Pane pane;
    private TabPane tabPane;

    private static VehicleGameScene _instance;

    public static VehicleGameScene getInstance() {
        if(_instance == null) {
            throw new Error("You need to run init before you use");
        }
        return _instance;
    }

    public static void init(Parent parent) {
        VehicleGameScene._instance = new VehicleGameScene(parent);
    }

    private VehicleGameScene(Parent parent) {

        // initial default UI
        // get UI object from parent
        this.pane = (Pane) parent.lookup("#gamePane");
        this.tabPane = (TabPane) parent.lookup("#tabPane");
        this.checkIn = (JFXButton) parent.lookup("#check_in");
        this.checkOut = (JFXButton) parent.lookup("#check_out");
        this.insertCard = (JFXButton) parent.lookup("#insert_card");
        this.pay = (JFXButton) parent.lookup("#pay");

        this.ticketIDLabel = (Label) parent.lookup("#ticketIDLabel");
        this.ticketTimeInLabel = (Label) parent.lookup("#ticketTimeInLabel");
        this.ticketPayTimeLabel = (Label) parent.lookup("#ticketPayTimeLabel");
        this.ticketPriceLabel = (Label) parent.lookup("#ticketPriceLabel");

        this.carStatusLabel = (Label) parent.lookup("#carStatus");
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

        if(this.checkIn == null) {
            throw new Error("Check In Button must not null");
        }

        if(this.checkOut == null) {
            throw new Error("Check Out Button must not null");
        }

        if(this.insertCard == null) {
            throw new Error("Insert Card Button Button must not null");
        }

        if(this.pay == null) {
            throw new Error("Pay Button must not null");
        }

        if(this.ticketIDLabel == null) {
            throw new Error("Ticket ID Label must not null");
        }

        if(this.ticketTimeInLabel == null) {
            throw new Error("Ticket Time In Label must not null");
        }

        if(this.ticketPayTimeLabel == null) {
            throw new Error("Ticket Pay Time Label must not null");
        }

        if(this.ticketPriceLabel == null) {
            throw new Error("Ticket Price Label must not null");
        }

        if(this.carStatusLabel == null) {
            throw new Error("Car Status Label must not null");
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

        carPark = new CarPark(pane, 120);
        carPark.generateParkingLot();

        checkIn.addEventFilter(MouseEvent.MOUSE_CLICKED, checkInHandler);
        checkOut.addEventFilter(MouseEvent.MOUSE_CLICKED, checkOutHandler);
        insertCard.addEventFilter(MouseEvent.MOUSE_CLICKED, insertCardHandler);
        pay.addEventFilter(MouseEvent.MOUSE_CLICKED, payHandler);

    }

    private EventHandler checkInHandler = e -> {
        System.out.println("Check In");
        gate.giveTicket(selectedCar.getVehicle());
        gate.open();
    };

    private EventHandler checkOutHandler = e -> {
        System.out.println("Check Out");

        Alert alert;
        CheckInStatus status = gate.checkOut(selectedCar.getVehicle());
        switch (status) {
            case UnPay:
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("");
                alert.setContentText("Your ticket haven't pay yet.\nPlease find the nearest payment machine to do the payment.");
                alert.showAndWait();
                break;

            case TimeExceed:
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Time Exceed");
                alert.setHeaderText("");
                alert.setContentText("Your check-out time has exceed.\nPlease repay your ticket.");
                alert.showAndWait();
                break;

            case Done:
                gate.open();
                break;
        }

    };

    private EventHandler insertCardHandler = e -> {
        System.out.println("Insert Card");
        if(selectedCar != null && selectedCar.hasTicket()) {
            // check the  car status
            CheckInTicket ticket = selectedCar.getTicket();
            paymentMachine.insertTicket(ticket);
            CheckInStatus status = paymentMachine.validTicket();
            double price = 0;
            switch (status) {
                case UnPay:
                    price = paymentMachine.calculatePrice();
                    ticketPriceLabel.setText(String.format("$%.2f", price));
                    break;

                case TimeExceed:
                    price = paymentMachine.calculatePriceTimeExceed();
                    ticketPriceLabel.setText(String.format("$%.2f", price));
                    break;

                case Done:
                    ticketPriceLabel.setText("Paid!!");
                    paymentMachine.returnTicket();
                    insertCard.setDisable(false);
                    pay.setDisable(true);
                    return;
            }
        }
    };

    private EventHandler payHandler = e -> {
        System.out.println("Pay");
        paymentMachine.payTicket();
        paymentMachine.returnTicket();
        insertCard.setDisable(false);
        pay.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("");
        alert.setTitle("Paid!");
        alert.setContentText("Your ticket has paid.\nPlease check-out in 15 minutes.");
        alert.showAndWait();
    };

    public int getCars() {
        return cars.size();
    }

//    public void setCarPark(CarPark carPark) {
//        this.carPark = carPark;
//    }

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
        if(selectedCar == null) {
            return;
        }

        if(e.getCode() == KeyCode.BACK_SPACE) {
            removeCar(selectedCar);
        }

        if(e.getCode() == KeyCode.S) {
            selectedCar.toggleReverse();
        }

        if(e.getCode() == KeyCode.W) {
            selectedCar.toggleStop();
        }

        if(e.getCode() == KeyCode.A) {
            selectedCar.rotateLeft();
        }

        if(e.getCode() == KeyCode.D) {
            selectedCar.rotateRight();
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
        addCar(car, 20, bound.getHeight() - 50);
    }

    private void addCar(GameObject object, double x, double y) {

        object
            .addEventFilter(MouseEvent.MOUSE_CLICKED,
            e -> {
                selectedCar = (Car) object;
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
        selectedCar = null;
    }

    private void onUpdate() {

        // close Gate after pass
        carPark.getGates().forEach(gate1 -> {
            boolean noCarNearGate = true;
            if (gate1.status) {
                for (GameObject car : cars) {
                    if(car.isColliding(gate1) != Side.NONE) {
                        noCarNearGate = false;
                        break;
                    }
                }
            }
            if(noCarNearGate) {
                gate1.close();
            }
        });

        carPark.getGates().forEach(gate1 -> {
            gate1.update();
        });

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

            // check Car collided
            if(carPark != null) {

                // collide with wall
                for (Wall wall : carPark.getWalls()) {
                    if(car.getMoveSide().compare(car.isColliding(wall))) {
                        collide = true;
                        break;
                    }
                }

                // collide with gate
                for (Gate gate : carPark.getGates()) {
                    if(!gate.status) {
                        if(car.getMoveSide().compare(car.isColliding(gate))) {
                            collide = true;
                            break;
                        }
                    }
                }

                // collide with payment machine
                for (PaymentMachine paymentMachine : carPark.getPaymentMachines()) {
                    if(car.getMoveSide().compare(car.isColliding(paymentMachine))) {
                        collide = true;
                        break;
                    }
                }

                // Is in carpark.
                carPark.getParkingLots().forEach(parkingLot -> {
                    if(parkingLot.isParkedBy(car)) {
                        return;
                    }
                });
            }

            if(!collide) {
                car.update();
            }
        });

        // check Car Near Payment Machine
        if(selectedCar != null && selectedCar instanceof Car) {
            Car selectedCar = this.selectedCar;

            // Check selectedCar Status
            if(selectedCar.isMove()) {
                if(selectedCar.isReverse()) {
                    carStatusLabel.setText("Reverse");
                } else {
                    carStatusLabel.setText("Forward");
                }
            } else {
                carStatusLabel.setText("Stop");
            }

            // Check selected Car has ticket or not
            if(selectedCar.hasTicket()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm");
                CheckInTicket ticket = selectedCar.getTicket();
                ticketIDLabel.setText(ticket.getId());
                ticketTimeInLabel.setText(formatter.format(ticket.getCheckInTime()));
                if(ticket.getPayTime() != null)
                    ticketPayTimeLabel.setText(formatter.format(ticket.getPayTime()));

            } else {
                resetTicketDetail();
            }

            // check gate (CheckIn Use)
            Gate gate = carPark.checkCarNearbyGate(selectedCar);
            if(gate != null) {
                this.gate = gate;
                // check has ticket or not
                boolean carStatus = gate.checkCarStatus(selectedCar);
                if(carStatus) {
                    checkIn.setDisable(true);
                    checkOut.setDisable(false);
                } else {
                    checkIn.setDisable(false);
                    checkOut.setDisable(true);
                }
            } else {
                this.gate = null;
                checkIn.setDisable(true);
                checkOut.setDisable(true);
            }

            // check payment machine
            PaymentMachine paymentMachine = carPark.checkCarNearbyPaymentMachine(selectedCar);
            if(paymentMachine != null) {
                this.paymentMachine = paymentMachine;
                // check has ticket or not
                boolean carStatus = paymentMachine.checkCarStatus(selectedCar);
                if(carStatus) {
                    if(paymentMachine.hasTicket()) {
                        insertCard.setDisable(true);
                        pay.setDisable(false);
                    } else {
                        insertCard.setDisable(false);
                        pay.setDisable(true);
                    }
                }
            } else {
                ticketPriceLabel.setText("N/A");
                insertCard.setDisable(true);
                pay.setDisable(true);
                if(this.paymentMachine != null)
                    this.paymentMachine.returnTicket();
            }
        } else {

            // reset to no selected car mode
            this.gate = null;
            this.paymentMachine = null;
            carStatusLabel.setText("N/A");
            insertCard.setDisable(true);
            pay.setDisable(true);
            checkIn.setDisable(true);
            checkOut.setDisable(true);
            resetTicketDetail();
        }
    }

    private void resetTicketDetail() {
        ticketIDLabel.setText("N/A");
        ticketTimeInLabel.setText("N/A");
        ticketPayTimeLabel.setText("N/A");
        ticketPriceLabel.setText("N/A");
    }

}
