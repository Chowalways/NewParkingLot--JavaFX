package main;

import Abstract.GameObject;
import CheckSystem.Other.Gender;
import Unit.*;

import Unit.Enum.Side;
import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersonGameScene {

    private List<GameObject> human = new ArrayList<>();
    private PersonObject selectedPerson = null;
    private Office office = null;
    private Door door = null;
    private PunchMachine punchMachine = null;

    private JFXButton CheckIn1;
    private JFXButton Checkout1;
    private Pane pane;
    private TabPane tabPane;
    private Label personStatus;
    private Label ticketStatus;
    private Label personIDLabel;
    private Label checkInTimeLabel;
    private Label checkOutTimeLabel;

    Scene scene;

    private static PersonGameScene _instance;

    public static PersonGameScene getInstance() {
        if(_instance == null) {
            throw new Error("You need to run init before you use");
        }
        return _instance;
    }

    public static void init(Parent parent) {
        PersonGameScene._instance = new PersonGameScene(parent);
    }

    private PersonGameScene(Parent parent) {

        // initial default UI
        // get UI object from parent
        this.pane = (Pane) parent.lookup("#gamePane1");
        this.tabPane = (TabPane) parent.lookup("#tabPane");
        this.CheckIn1 = (JFXButton) parent.lookup("#CheckIn1");
        this.Checkout1 = (JFXButton) parent.lookup("#Checkout1");
        this.personStatus = (Label) parent.lookup("#personStatusLabel");
        this.ticketStatus = (Label) parent.lookup("#ticketStatusLabel");
        this.personIDLabel = (Label) parent.lookup("#personIDLabel");
        this.checkInTimeLabel = (Label) parent.lookup("#checkInTimeLabel");
        this.checkOutTimeLabel = (Label) parent.lookup("#checkOutTimeLabel");

        this.scene = parent.getScene();

        if(this.pane == null) {
            throw new Error("Pane is null");
        }

        if(this.tabPane == null) {
            throw new Error("Tab pane is null");
        }

        if(this.scene == null) {
            throw new Error("Scene is null");
        }

        if(this.CheckIn1 == null) {
            throw new Error("Check In Button is null");
        }

        if(this.Checkout1 == null) {
            throw new Error("Check Out Button is null");
        }

        if(this.personStatus == null){
            throw new Error("Person Status Label is null");
        }

        if(this.ticketStatus == null){
            throw new Error("Ticket Status Label is null");
        }

        if(this.personIDLabel == null){
            throw new Error("Person ID Label is null");
        }

        if(this.checkInTimeLabel == null){
            throw new Error("Check In Time Label is null");
        }

        if(this.checkOutTimeLabel == null){
            throw new Error("Check Out Time Label is null");
        }

        this.tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // set Event Listener with to selected tab
                    if(newValue.getId().compareToIgnoreCase("workSystem") == 0) {
                        setHumanControl();
                    } else if (newValue.getId().compareToIgnoreCase("carSystem") == 0) {
                        removeHumanControl();
                    }
                }
        );

        this.personStatus.setText("0 Person");
        this.ticketStatus.setText("ticket");

        setHumanControl();

        office = new Office(pane);
        office.generateOffice();

        CheckIn1.addEventFilter(MouseEvent.MOUSE_CLICKED, checkIn1Handler);
        Checkout1.addEventFilter(MouseEvent.MOUSE_CLICKED, checkOut1Handler);
    }

    private EventHandler checkIn1Handler = e -> {
        System.out.println("Check In");
        //this need to put at when get in to checking room then check in with punch card. But having some problems.
        door.checkinCard(selectedPerson.getPerson());
        checkInWithPhone();
        door.open();
    };

    private EventHandler checkOut1Handler = e -> {
        System.out.println("Check Out");
        checkOutWithPhone();
        door.open();
        //Person check out by Phone no need to checking ticket.
//        Alert alert;
//        CheckInStatus status = door.checkOut(selectedPerson.getPerson());
//        switch (status) {
//            case Done:
//                door.open();
//                break;
//            default:
//                door.close();
//                alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Remind");
//                alert.setHeaderText("");
//                alert.setContentText("You haven't Punch Card.\n Please go to Punch Card for Check Out");
//                alert.showAndWait();
//                break;
//        }
    };

    void generatePerson() {
        System.out.println("Generate Person");
        Bounds bound = pane.getLayoutBounds();

        PersonObject pObj = PersonObject.createPerson(String.format("Person%08d", human.size() + 1), Gender.MALE,28);
        Bounds personBound = pObj.getView().getLayoutBounds();
        addPersonObj(pObj, 500, bound.getHeight() - 50);
    }

    public int getPerson(){
        return human.size();
    }

    public EventHandler events = (EventHandler<KeyEvent>) e -> {
        //Game Start with number keypad 7;
        if(e.getCode() == KeyCode.NUMPAD7) {
            generatePerson();
            this.personStatus.setText(String.format("%d Person" ,getPerson()));
        }

        //remove person by number keypad 9;
        if(e.getCode() == KeyCode.NUMPAD9) {
            removePersonObject(selectedPerson);
            this.personStatus.setText(String.format("%d Person" ,getPerson()));
        }

        // go back with number keypad 5;
        if(e.getCode() == KeyCode.NUMPAD5) {
            selectedPerson.toggleReverse();
        }

        //button stop is number keypad 8;
        if(e.getCode() == KeyCode.NUMPAD8) {
            selectedPerson.toggleStop();
        }

        //move to left using number keypad4;
        if(e.getCode() == KeyCode.NUMPAD4) {
            selectedPerson.rotateLeft();
        }

        //move to right using number keypad6
        if(e.getCode() == KeyCode.NUMPAD6) {
            selectedPerson.rotateRight();
        }
    };

    public void setHumanControl() {
        // Set Control
        scene.setOnKeyPressed(events);
    }

    public void removeHumanControl() {
        // remove eventHandler
        scene.setOnKeyPressed(null);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());
    }

    private void addPersonObj(GameObject object, double x, double y) {

        object
                .addEventFilter(MouseEvent.MOUSE_CLICKED,
                        e -> {
                            selectedPerson = (PersonObject) object;
                        });

        human.add(object);
        addGameObject(object, x, y);
    }

    private void removePersonObject(GameObject object) {
        System.out.println("remove car");
        human.remove(object);
        //need to remove person;
        pane.getChildren().remove(object.getView());
        selectedPerson = null;
    }

    public void startTimer() {
        // Update scene timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }

    private void onUpdate() {

        // close Gate after pass
        office.getDoor().forEach(door1 -> {
            boolean noPersonNearbyDoor = true;
            if (door1.getDoorStatus()) {
                for (GameObject person : human) {
                    if (person.isColliding(door1) != Side.NONE) {
                        noPersonNearbyDoor = false;
                        break;
                    }
                }
            }
            if (noPersonNearbyDoor) {
                door1.close();
            }

        });

        office.getDoor().forEach(door1 -> {
            door1.update();
        });

        human.removeIf(GameObject::isDead);

        human.forEach(humans -> {
            boolean collide = false;
            for (GameObject person : human) {
                if (humans.getMoveSide().compare(humans.isColliding(person))) {
                    collide = true;
                    break;
                }
            }

            // check person collided
            if(office != null) {

                // collide with wall
                for (Wall wall : office.getWalls()) {
                    if(humans.getMoveSide().compare(humans.isColliding(wall))) {
                        collide = true;
                        break;
                    }
                }

                // collide with door
                for (Door door : office.getDoor()) {
                    if(!door.getDoorStatus()) {
                        if(humans.getMoveSide().compare(humans.isColliding(door))) {
                            collide = true;
                            break;
                        }
                    }
                }

                // collide with punch machine change to punch card machine.
                for (PunchMachine punchMachine : office.getPunchCardMachines()) {
                    if(office.getMoveSide().compare(office.isColliding(punchMachine))) {
                        collide = true;
                        break;
                    }
                }
            }

            if(!collide) {
                humans.update();
            }
        });

        if (selectedPerson != null && selectedPerson instanceof PersonObject) {
            PersonObject selectedPerson = this.selectedPerson;

            // check door (CheckIn Use)
            Door door = office.checkPersonNearbyDoor(selectedPerson);
            if (door != null) {
                this.door = door;
                // check has ticket or not
                boolean status = door.personStatus(selectedPerson);
                if (status) {
                    CheckIn1.setDisable(true);
                    Checkout1.setDisable(false);
                } else {
                    CheckIn1.setDisable(false);
                    Checkout1.setDisable(true);
                }
            } else {
                this.door = null;
                CheckIn1.setDisable(true);
                Checkout1.setDisable(true);
            }
        } else {
            // reset to default
            this.door = null;
            this.punchMachine = null;
            this.CheckIn1.setDisable(true);
            this.Checkout1.setDisable(true);

            resetTicketDetail();
        }
    }

    private void resetTicketDetail() {
        personIDLabel.setText("");
        checkInTimeLabel.setText("");
        checkOutTimeLabel.setText("");
        ticketStatus.setText("");
    }

    private void checkInWithPhone(){
        this.ticketStatus.setText("GET IN OFFICE");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        checkInTimeLabel.setText(formatter.format(door.getPersonCheckInTime()));
    }

    private void checkOutWithPhone(){
        this.ticketStatus.setText("CHECK OUT");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        checkOutTimeLabel.setText(formatter.format(door.getPersonCheckOutTime()));
    }
}
