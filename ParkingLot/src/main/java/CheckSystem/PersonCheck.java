package CheckSystem;

import Abstract.BasicObj;
import Abstract.CheckInTicket;
import CheckSystem.Other.Check;
import CheckSystem.Other.CheckInStatus;
import config.ConfigManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PersonCheck implements Check {

    private double pricePerHour;

    public PersonCheck(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public CheckInTicket signIn(BasicObj obj, String id){
        PersonCheckInTicket ticket =
                new PersonCheckInTicket(id, LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))));

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
