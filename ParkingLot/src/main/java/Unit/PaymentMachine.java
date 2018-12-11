package Unit;

import Abstract.CheckInTicket;
import CheckSystem.Other.CheckInStatus;
import CheckSystem.CheckInSystem;
import Unit.Enum.Direction;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import Abstract.GameObject;

public class PaymentMachine extends GameObject {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 30;

    private CheckInSystem checkInSystem;
    private CheckInTicket ticket;

    public boolean hasTicket() {
        return ticket != null;
    }

    private PaymentMachine(Node view) {
        super(view);
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

    public void connectSystem(CheckInSystem checkInSystem) {
        // connect to the server
        this.checkInSystem = checkInSystem;
    }

    public boolean checkCarStatus(Car car) {
        return car.hasTicket();
    }

    public boolean checkTicketStatus(Car car) {
        return car.hasTicket();
    }

    public void insertTicket(CheckInTicket ticket) {
        this.ticket = ticket;
    }

    public CheckInStatus validTicket() {
        return checkInSystem.validTicket(ticket);
    }

    public double calculatePrice() {
        return checkInSystem.calculatePrice(ticket);
    }

    public double calculatePriceTimeExceed() {
        return checkInSystem.calculatePriceExceed(ticket);
    }

    public void payTicket() {
        checkInSystem.payTicket(ticket);
    }

    public void returnTicket() {
        ticket = null;
    }

}
