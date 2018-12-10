package Unit;

import Unit.Enum.Direction;
import function.CheckInSystem;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import sample.GameObject;

public class PaymentMachine extends GameObject {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 30;
    private CheckInSystem checkInSystem;


    private PaymentMachine(Node view) {
        super(view);
        checkInSystem = new CheckInSystem();
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

        return new PaymentMachine(imageView);
    }

}
