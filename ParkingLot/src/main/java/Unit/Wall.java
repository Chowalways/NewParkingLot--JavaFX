package Unit;

import Unit.Enum.Direction;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.GameObject;

public class Wall extends GameObject {

    public static final int SHORT = 5;
    public static final int LONG = 20;

    private Wall(Node view) {
        super(view);
    }

    public static Wall creteWall(Direction direction) {
        Rectangle rectangle;
        if(direction == Direction.HORIZONTAL) {
            rectangle = new Rectangle(LONG, SHORT);
        } else {
            rectangle = new Rectangle(SHORT, LONG);
        }
        rectangle.setStrokeWidth(2);
        rectangle.setStroke(Color.GRAY);
        return new Wall(rectangle);
    }
}
