package CheckSystem;

import Abstract.BasicObj;
import Abstract.CheckInTicket;
import CheckSystem.Other.Check;
import CheckSystem.Other.CheckInStatus;
import config.ConfigManager;

import Class.Vehicle;
import Class.Person;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class CheckInSystem {

    protected Check check;
    // TO KEEP PROPER TRACK OF ALL ACTIVITY
    public static ArrayList<CheckInTicket> tickets = new ArrayList<>();
    public static ArrayList<CheckInTicket> pInTickets = new ArrayList<>();
    public static ArrayList<CheckInTicket> pOutTickets = new ArrayList<>();
    public static ArrayList<CheckInTicket> vInTickets = new ArrayList<>();
    public static ArrayList<CheckInTicket> vOutTickets = new ArrayList<>();

    public CheckInSystem(Check check) {
        this.check = check;
    }

    //GET TICKET COUNT FOR EACH TYPE OF TICKET
    public static int getTicketCount(String arg){
        if(arg.equalsIgnoreCase("pin")) {
            return pInTickets.size();
        }else if(arg.equalsIgnoreCase("pout")){
            return pOutTickets.size();
        }else
        if(arg.equalsIgnoreCase("vin")) {
            return vInTickets.size();
        }else if(arg.equalsIgnoreCase("vout")){
            return vOutTickets.size();
        }
        return 0;
    }

    //GIVE TICKET BASED ON WHO IS REQUESTING IT
    public final void giveTicket(BasicObj basic) {
       if(Vehicle.class.isInstance(basic)){
           CheckInTicket ticket = check.signIn(basic, String.format("D%06d", vInTickets.size() + 1));
           basic.setTicket(ticket.clone());
           this.vInTickets.add(ticket);
           this.tickets.add(ticket);
       }
       if(Person.class.isInstance(basic)){
           CheckInTicket ticket = check.signIn(basic, String.format("D%06d", pInTickets.size() + 1));
           basic.setTicket(ticket.clone());
           this.pInTickets.add(ticket);
           this.tickets.add(ticket);
       }

        // if not clone it will direct use the address,
        // ex: when user do payment will also change the ticket in Check In System Ticket Array.

        //this.tickets.add(ticket);
    }

    public CheckInStatus validTicket(CheckInTicket ticket) {
        CheckInTicket updateTicket = findTicket(ticket);

        if(updateTicket == null) {
            throw new Error("Invalid Ticket");
        }

        // validate ticket from system data
        CheckInStatus status = check.validate(updateTicket);

        return status;
    }

    public double calculatePrice(CheckInTicket ticket) {
        return check.calculatePrice(ticket);
    }

    public double calculatePriceExceed(CheckInTicket ticket) {
        return check.calculatePriceExceed(ticket);
    }

    private CheckInTicket findTicket(CheckInTicket ticket) {
        for (CheckInTicket checkInTicket : tickets) {
            if(ticket.getId().compareToIgnoreCase(checkInTicket.getId()) == 0)
                return checkInTicket;
        }
        return null;
    }

    public void payTicket(CheckInTicket ticket) {
        CheckInTicket updateTicket = findTicket(ticket);
        updateTicket.setPayTime(LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));
        ticket.setPayTime(updateTicket.getPayTime());
    }

    public CheckInStatus checkOut(CheckInTicket ticket, String arg) {
        CheckInTicket updateTicket = findTicket(ticket);
        if(arg.equalsIgnoreCase("veh"))
            vOutTickets.add(updateTicket);
        if(arg.equalsIgnoreCase("per"))
            pOutTickets.add(updateTicket);
        CheckInStatus status = validTicket(updateTicket);

        if(status == CheckInStatus.Done) {
            updateTicket.checkOut(LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));
        }
        return status;
    }

}
