package Unit;

import Abstract.CheckInTicket;
import Abstract.GameObject;
import CheckSystem.CheckInSystem;
import CheckSystem.Other.CheckInStatus;
import Unit.Enum.Direction;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class PunchMachine extends GameObject {

    public static final int PUNCHWIDTH = 30;
    public static final int PUNCHHEIGHT = 30;

    private CheckInSystem checkInSystem;
    private CheckInTicket ticket;

    public PunchMachine(Node view) {
        super(view);
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public static PunchMachine createPunchMachine(Direction direction) {
        ImageView imageView;
        imageView = new ImageView("res/images/time.png");
        if(direction == Direction.HORIZONTAL) {
            imageView.setFitHeight(PUNCHHEIGHT);
            imageView.setFitWidth(PUNCHWIDTH);
        } else {
            imageView.setFitHeight(PUNCHWIDTH);
            imageView.setFitWidth(PUNCHHEIGHT);
        }

        return new PunchMachine(imageView);
    }

    public void connectSystem(CheckInSystem checkInSystem) {
        // connect to the server
        this.checkInSystem = checkInSystem;
    }

    public boolean checkPersonStatus(PersonObject person) {
        return person.hasTicket();
    }

    public void insertTicket(CheckInTicket ticket) {
        this.ticket = ticket;
    }

    public CheckInStatus validTicket() {
        return checkInSystem.validTicket(ticket);
    }

    public void returnTicket() {
        ticket = null;
    }
}
