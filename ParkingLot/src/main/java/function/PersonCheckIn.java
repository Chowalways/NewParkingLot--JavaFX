package function;

import java.util.ArrayList;
import java.util.List;

public class PersonCheckIn implements CheckIn {

    protected String personId;
    private BasicObj obj;

    public List<CheckInTicket> ticketHistory = new ArrayList();

    public CheckInTicket signIn(BasicObj obj){
        this.obj = obj;
        obj.ticket =  CheckInTicket.getTicket();
        return obj.ticket;
    }

    public boolean checkout(CheckInTicket ticket){
        if(ticketHistory.contains(ticket) && ticket.pay){
            return true;
        }else{
            return false;
        }
    }

//    public boolean signOut(CheckInTicket ticket) {
//
//        if (ticketHistory.contains(ticket) && ticket.pay) {
//            ticketHistory.add(this.obj.ticket);
//            return true;
//        } else {
//            System.out.println("Ticket invalid (not in history)");
//            return false;
//        }
//    }
}
