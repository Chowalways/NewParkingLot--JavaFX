package main;

import Abstract.CheckInTicket;
import Abstract.GameObject;
import Abstract.GameScene;
import CheckSystem.Other.Gender;
import Unit.*;
import Class.Person; // TO CHANGE
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

public class PersonGameScene extends GameScene {

    private List<GameObject> human = new ArrayList<>();
    private PersonObject selectedPerson = null;
    private Office office;
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
        super(parent);
    }

    @Override
    protected void preInit(Parent parent) {
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

        pane.sceneProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("here");
            System.out.println(newValue);
        }));
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

        this.personStatus.setText("0 Person");
        this.ticketStatus.setText("");

    }

    @Override
    protected void generate() {

        office = new Office(pane);
        office.generateOffice();

    }

    @Override
    protected void bindControl() {
        System.out.println(checkIn1Handler);
//        CheckIn1.addEventFilter(MouseEvent.MOUSE_CLICKED, checkIn1Handler);
//        Checkout1.addEventFilter(MouseEvent.MOUSE_CLICKED, checkOut1Handler);
    }

    @Override
    protected void onUpdate() {

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
//                for (PunchMachine punchMachine : office.getPunchCardMachines()) {
//                    if(office.getMoveSide().compare(office.isColliding(punchMachine))) {
//                        collide = true;
//                        break;
//                    }
//                }
            }

            if(!collide) {
                humans.update();
            }
        });

        if(selectedPerson == null){
            return;
        }

        if (selectedPerson != null && selectedPerson instanceof PersonObject) {
            PersonObject selectedPerson = this.selectedPerson;
            personIDLabel.setText(selectedPerson.getPerson().getId());

            //has ticket means customer have check in status or not.
            if(selectedPerson.hasTicket()) {
                this.ticketStatus.setText("GET IN OFFICE");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm");
                CheckInTicket ticket = selectedPerson.getTicket();
//                personIDLabel.setText(selectedPerson.getPerson().getId());
                checkInTimeLabel.setText(formatter.format(ticket.getCheckInTime()));
            } else {
//                resetTicketDetail();
            }

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


    private EventHandler checkIn1Handler = e -> {
        System.out.println("Check In");
        //this need to put at when get in to checking room then check in with punch card. But having some problems.
        door.checkinCard(selectedPerson.getPerson());
        door.open();
    };

    private EventHandler checkOut1Handler = e -> {
        System.out.println("Check Out");
        door.checkOut(selectedPerson.getPerson()); //ADDED FOR CHECKING OUT
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
        PersonObject pObj = PersonObject.createPerson(String.format("P%05d", human.size() + 1), Gender.MALE,28);
        Bounds personBound = pObj.getView().getLayoutBounds();
        addPersonObj(pObj, personBound.getHeight() + 100, bound.getHeight() - 200);
    }

    public int getPerson(){
        return human.size();
    }

    public EventHandler events = (EventHandler<KeyEvent>) e -> {
        //Game Start with ENTER;
        if(e.getCode() == KeyCode.ENTER) {
            generatePerson();
            this.personStatus.setText(String.format("%d Person" ,getPerson()));
        }

        if(selectedPerson != null) {
            //remove person by BACK_SPACE;
            if(e.getCode() == KeyCode.BACK_SPACE) {
                removePersonObject(selectedPerson);
                this.personStatus.setText(String.format("%d Person" ,getPerson()));
            }

            // go back with KEY S;
            if(e.getCode() == KeyCode.S) {
                selectedPerson.toggleReverse();
            }

            //button stop with KEY W;
            if(e.getCode() == KeyCode.W) {
                selectedPerson.toggleStop();
            }

            //move to left with KET A;
            if(e.getCode() == KeyCode.A) {
                selectedPerson.rotateLeft();
            }

            //move to right with KEY D
            if(e.getCode() == KeyCode.D) {
                selectedPerson.rotateRight();
            }
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
        if(object == null)
            return;
        System.out.println("removed");
        human.remove(object);
        //need to remove person;
        pane.getChildren().remove(object.getView());
        selectedPerson = null;
    }

    private void resetTicketDetail() {
        personIDLabel.setText("N/A");
        checkInTimeLabel.setText("N/A");
        checkOutTimeLabel.setText("N/A");
        ticketStatus.setText("N/A");
    }

    private void checkOutWithPhone(){
        this.ticketStatus.setText("CHECK OUT");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm");
        checkOutTimeLabel.setText(formatter.format(door.getPersonCheckOutTime()));
    }
}
