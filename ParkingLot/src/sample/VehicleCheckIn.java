package sample;

import java.util.ArrayList;
import java.util.List;

public class VehicleCheckIn {

    protected String vehicleId;
    public List ticketHistory = new ArrayList();

    public CheckInTicket signIn(BasicObj obj){
        obj.ticket =  CheckInTicket.getTicket();
        ticketHistory.add(obj.ticket);
        return obj.ticket;
    }

    public void signOut(CheckInTicket ticket) {
        if (ticketHistory.contains(ticket)) {
            ticketHistory.remove(ticket);
        } else {
            System.out.println("Ticket invalid (not in history)");
        }
    }
}
