package function;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class CheckInSystem {

    protected Check check;
    public ArrayList<CheckInTicket> tickets = new ArrayList<>();

    public CheckInSystem(Check check) {
        this.check = check;
    }

    public final void giveTicket(BasicObj basic) {
        CheckInTicket ticket = check.signIn(basic, String.format("D%06d", tickets.size() + 1));

        // if not clone it will direct use the address,
        // ex: when user do payment will also change the ticket in Check In System Ticket Array.
        basic.setTicket(ticket.clone());
        this.tickets.add(ticket);
    }

    public CheckInStatus insertTicket(CheckInTicket ticket) {
        CheckInTicket updateTicket = findTicket(ticket);

        if(updateTicket == null) {
            throw new Error("Invalid Ticket");
        }

        CheckInStatus status = check.validate(ticket);

        if(status == CheckInStatus.Done) {
            updateTicket.payDate = ticket.payDate;
            check.checkout(updateTicket);
        }
        return status;
    }

    public double calculatePrice(CheckInTicket ticket) {
        return check.calculatePrice(ticket);
    }

    private CheckInTicket findTicket(CheckInTicket ticket) {
        for (CheckInTicket checkInTicket : tickets) {
            if(ticket.id.compareToIgnoreCase( checkInTicket.id) == 0)
                return checkInTicket;
        }
        return null;
    }

}
