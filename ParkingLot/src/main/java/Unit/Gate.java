package Unit;

import Unit.Enum.Direction;
import Unit.Interface.Openable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.GameObject;

public class Gate extends GameObject implements Openable {

    public static final int BodyWidth = 15;
    public static final int BodyHeight = 25;
    public static final int ArmWidth = 35;
    public static final int ArmHeight = 5;
    public Boolean status = false;
    public Rectangle arm;

    private Gate(Node view) {
        super(view);
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
        return new Gate(pane);
    }


    @Override
    public void open() {
        status = true;
    }

    @Override
    public void closse() {
        status = false;
    }
}
