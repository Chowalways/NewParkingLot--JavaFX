package function;

import config.ConfigManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

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
            if(ticket.id.compareToIgnoreCase( checkInTicket.id) == 0)
                return checkInTicket;
        }
        return null;
    }

    public void payTicket(CheckInTicket ticket) {
        CheckInTicket updateTicket = findTicket(ticket);
        updateTicket.payTime = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
        ticket.payTime = updateTicket.payTime;
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
