package Unit;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Abstract.GameObject;
import Unit.Enum.Side;

public class ParkingLot extends GameObject {

        GameObject car;
        public static final int WIDTH = 30;
        public static final int HEIGHT = 30;


        private ParkingLot(Rectangle rectangle) {
            super(rectangle);
        }

        public static ParkingLot createParkingLot() {
            Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);

            rectangle.setFill(Color.GREEN);
            rectangle.setStrokeWidth(2);
            rectangle.setStroke(Color.GRAY);

            return new ParkingLot(rectangle);
        }

        public void setStatus(boolean status) {
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
