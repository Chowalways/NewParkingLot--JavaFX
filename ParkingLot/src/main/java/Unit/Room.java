package Unit;

import Abstract.GameObject;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Room extends GameObject {

    GameObject human;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;

    private Room(Rectangle rectangle) {
        super(rectangle);
    }

    public static Room createRoom() {
        Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);

//        rectangle.setFill(Color.GREEN);
        rectangle.setStrokeWidth(2);
//        rectangle.setStroke(Color.GRAY);

        return new Room(rectangle);
    }

    public void setStatus(boolean status) {
//        if(status)
//            ((Rectangle) getView()).setFill(Color.RED);
//        else {
////            ((Rectangle) getView()).setFill(Color.GREEN);
//            human = null;
//        }
    }



}
