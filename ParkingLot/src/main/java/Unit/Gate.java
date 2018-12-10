package Unit;

import Unit.Enum.Direction;
import Unit.Interface.Openable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.GameObject;

public class Gate extends GameObject implements Openable {

    public static final int BodyWidth = 15;
    public static final int BodyHeight = 25;
    public static final int ArmWidth = 35;
    public static final int ArmHeight = 5;

    public Boolean status = false;
    public Rectangle arm;

    private Gate(Node view, Rectangle arm) {
        super(view);
        this.arm = arm;
    }

    public static Gate createGate(Direction direction) {
        Pane pane = new Pane();
        Rectangle body;
        Rectangle arm;
        if(direction == Direction.HORIZONTAL) {
            body = new Rectangle(BodyWidth, BodyHeight);
            arm = new Rectangle(ArmWidth, ArmHeight);
        } else {
            body = new Rectangle(BodyHeight, BodyWidth);
            arm = new Rectangle(ArmHeight, ArmWidth);
        }
        body.setFill(Color.RED);
        arm.setFill(Color.GRAY);

        arm.setTranslateX(10);
        arm.setTranslateY(5);
        pane.getChildren().add(body);
        pane.getChildren().add(arm);
        return new Gate(pane, arm);
    }

    public boolean checkCarStatus(Car car) {
        return car.hasTicket();
    }

    @Override
    public void open() {
        status = true;
    }

    @Override
    public void close() {
        status = false;
    }

    public void update() {
        if(arm == null)
            return;
        Pane pane = (Pane) getView();
        if(status) {
//            if(pane.getChildren().contains(arm))
//                pane.getChildren().remove(arm);
            arm.setVisible(false);
        } else {
//            if(!pane.getChildren().contains(arm))
//                pane.getChildren().add(arm);
            arm.setVisible(true);
        }
    }
}
