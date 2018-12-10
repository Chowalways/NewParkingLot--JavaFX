package Unit;

import Unit.Enum.Direction;
import function.Check;
import function.CheckInSystem;
import function.VehicleCheck;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import main.GameObject;

public class PaymentMachine extends GameObject {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 30;
    private CheckInSystem checkInSystem;


    private PaymentMachine(Node view, double pricePerHour) {
        super(view);
        Check vehicleCheck = new VehicleCheck(100);
        checkInSystem = new CheckInSystem(vehicleCheck);
    }

    public static PaymentMachine create(Direction direction) {
        ImageView imageView;
        imageView = new ImageView("res/images/payment-machine.png");
        if(direction == Direction.HORIZONTAL) {
            imageView.setFitHeight(HEIGHT);
            imageView.setFitWidth(WIDTH);
        } else {
            imageView.setFitHeight(WIDTH);
            imageView.setFitWidth(HEIGHT);
        }

        return new PaymentMachine(imageView, 100);
    }

    public boolean checkCarStatus(Car car) {
        return car.hasTicket();
    }

}
