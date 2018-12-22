package Unit;

import Abstract.BasicObj;
import Abstract.CheckInTicket;
import Abstract.GameObject;
import CheckSystem.CheckInSystem;
import CheckSystem.Other.CheckInStatus;
import CheckSystem.PersonCheckInTicket;
import Unit.Enum.Direction;
import Unit.Interface.Openable;
import config.ConfigManager;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Door extends GameObject implements Openable {

    private static final int DOORWIDTH = 10;
    private static final int DOORHEIGHT = 15;
    private static final int ARMHEIGHT = 8;
    private static final int ARMWIDTH = 35;

    private CheckInSystem checkInSystem;
    private PersonCheckInTicket personCheckInTicket;
    private Boolean doorStatus = false;
    private Rectangle arm;
    private Rectangle doorBody;
    private LocalDateTime currentPoint;

    public Door(Node view, Rectangle arm, Rectangle doorBody) {
        super(view);
        this.arm = arm;
        this.doorBody = doorBody;
    }

    public static Door createDoor(Direction direction){
        Pane pane2 = new Pane();
        Rectangle doorBody;
        Rectangle arm;
        if(direction == Direction.HORIZONTAL) {
            doorBody = new Rectangle(DOORWIDTH, DOORHEIGHT);
            arm = new Rectangle(ARMWIDTH, ARMHEIGHT);
        }else {
            doorBody = new Rectangle(DOORHEIGHT, DOORWIDTH);
            arm = new Rectangle(ARMHEIGHT, ARMWIDTH);
        }
        doorBody.setFill(Color.BLUE);
        arm.setFill(Color.BLUE);

        arm.setTranslateX(8);
        arm.setTranslateY(5);
        arm.setRotate(0);
        doorBody.setRotate(0);
        pane2.getChildren().add(doorBody);
        pane2.getChildren().add(arm);
        return new Door(pane2, arm, doorBody);
    }

    public void connectSystem(CheckInSystem checkInSystem) {
        this.checkInSystem = checkInSystem;
    }

    public boolean personStatus(PersonObject person) {
        return person.hasTicket();
    }

    public Boolean getDoorStatus(){
        return this.doorStatus;
    }

    public void checkinCard(BasicObj basicObj) {
        this.currentPoint = LocalDateTime.now(); //直接使用LocalDateTime類別來取得日期與時間
        checkInSystem.giveTicket(basicObj);
        this.personCheckInTicket = new PersonCheckInTicket(basicObj.getId(), currentPoint);
    }

    public CheckInStatus checkOut(BasicObj basicObj) {
        CheckInTicket ticket = basicObj.getTicket();
        CheckInStatus status = checkInSystem.checkOut(ticket);
        if(status == CheckInStatus.Done) {
            basicObj.setTicket(null);
        }
        return status;
    }

    public LocalDateTime getPersonCheckInTime(){
        return personCheckInTicket.getCheckInTime();
    }

    public LocalDateTime getPersonCheckOutTime(){
        LocalDateTime currentPoint = LocalDateTime.now(); //直接使用LocalDateTime類別來取得日期與時間
        personCheckInTicket.setCheckOutTime(currentPoint);
        return personCheckInTicket.getCheckOutTime();
    }

    public String getPersonId(BasicObj basicObj){
        return basicObj.getId();
    }

    @Override
    public void open() {
        doorStatus = true;
        System.out.println("Door opened");
    }

    @Override
    public void close() {
        doorStatus = false;
//        System.out.println("Door closed");
    }

    public void update(){
        if(arm == null){
            return;
        }
        if(doorStatus){
            arm.setVisible(false);
//            doorBody.setVisible(false);
//            arm.setRotate(90);
        }
        else{
            arm.setVisible(true);
//            arm.setRotate(0);
//            doorBody.setVisible(true);
        }
    }
}
