package function;

import config.ConfigManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonCheck implements Check {

    protected String personId;
    private double pricePerHour;
    private BasicObj obj;

    public PersonCheck(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public List<CheckInTicket> ticketHistory = new ArrayList();

    public CheckInTicket signIn(BasicObj obj, String id){
        this.obj = obj;
        PersonCheckInTicket ticket =
                new PersonCheckInTicket("0", LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));

        obj.setTicket(ticket);
        return ticket;
    }

    public boolean checkout(CheckInTicket ticket){
        ticket.checkOut(LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));
        return true;
    }

    @Override
    public CheckInStatus validate(CheckInTicket ticket) {

        return CheckInStatus.Done;
    }

    @Override
    public double calculatePrice(CheckInTicket ticket) {
        return 0;
    }

    @Override
    public double calculatePriceExceed(CheckInTicket ticket) {
        return 0;
    }
}
