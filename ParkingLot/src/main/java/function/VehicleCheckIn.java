package function;

import java.util.ArrayList;
import java.util.List;

public class VehicleCheckIn {

    protected String vehicleId;
    protected List ticketHistory = new ArrayList();

    public CheckInTicket signIn(BasicObj obj){
        CheckInTicket ticket = CheckInTicket.getTicket();
        obj.setTicket(ticket);
        ticketHistory.add(ticket);
        return ticket;
    }

    public void signOut(CheckInTicket ticket) {
        if (ticketHistory.contains(ticket)) {
            ticketHistory.remove(ticket);
        } else {
            System.out.println("Ticket invalid (not in history)");
        }
    }
}
