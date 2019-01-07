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
    // TO KEEP PROPER TRACK OF ALL ACTIVITY (MOCK Database)
    public static ArrayList<CheckInTicket> vehicleCheckInTickets = new ArrayList<>();
    public static ArrayList<CheckInTicket> personCheckInTickets = new ArrayList<>();

    public CheckInSystem(Check check) {
        this.check = check;
    }

    //GIVE TICKET BASED ON WHO IS REQUESTING IT
    public final void giveTicket(BasicObj basic) {
       if(Vehicle.class.isInstance(basic)){
           CheckInTicket ticket = check.signIn(basic, String.format("D%06d", vehicleCheckInTickets.size() + 1));
           basic.setTicket(ticket.clone());
           this.vehicleCheckInTickets.add(ticket);
       }
       if(Person.class.isInstance(basic)){
           CheckInTicket ticket = check.signIn(basic, String.format("D%06d", personCheckInTickets.size() + 1));
           basic.setTicket(ticket.clone());
           this.personCheckInTickets.add(ticket);
       }

        // if not clone it will direct use the address,
        // ex: when user do payment will also change the ticket in Check In System Ticket Array.

        //this.vehicleCheckInTickets.add(ticket);
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
        if (ticket instanceof VehicleCheckInTicket) {
            for (CheckInTicket checkInTicket : vehicleCheckInTickets) {
                if (ticket.getId().compareToIgnoreCase(checkInTicket.getId()) == 0)
                    return checkInTicket;
            }
        } else {
            for (CheckInTicket checkInTicket: personCheckInTickets) {
                if (ticket.getId().compareToIgnoreCase(checkInTicket.getId()) == 0)
                    return checkInTicket;
            }
        }
        return null;
    }

    public void payTicket(CheckInTicket ticket) {
        CheckInTicket updateTicket = findTicket(ticket);
        updateTicket.setPayTime(LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));
        ticket.setPayTime(updateTicket.getPayTime());
    }

    public CheckInStatus checkOut(CheckInTicket ticket) {
        CheckInTicket updateTicket = findTicket(ticket);
        CheckInStatus status = validTicket(updateTicket);

        if(status == CheckInStatus.Done) {
            updateTicket.checkOut(LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));
        }
        return status;
    }

}
