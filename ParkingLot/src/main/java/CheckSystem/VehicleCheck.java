package CheckSystem;

import Abstract.BasicObj;
import Abstract.CheckInTicket;
import CheckSystem.Other.Check;
import CheckSystem.Other.CheckInStatus;
import CheckSystem.Other.CheckInType;
import config.ConfigManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class VehicleCheck implements Check {

    double pricePerHour;

    public VehicleCheck(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    @Override
    public CheckInTicket signIn(BasicObj obj, String id) {
        CheckInTicket ticket = new VehicleCheckInTicket(id, LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE"))), CheckInType.GUEST);
        obj.setTicket(ticket);
        return ticket;
    }

    @Override
    public boolean checkout(CheckInTicket ticket) {
        ticket.checkOut(LocalDateTime.now());
        return true;    }

    @Override
    public CheckInStatus validate(CheckInTicket ticket) {

        if(!ticket.isPaid()) {
            return CheckInStatus.UnPay;
        }

        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));

        // exceed 15 minutes
        if(currentDate.getMinute() - ticket.getPayTime().getMinute()  > 15 ) {
            return CheckInStatus.TimeExceed;
        }

        return CheckInStatus.Done;
    }

    @Override
    public double calculatePrice(CheckInTicket ticket) {

        double price = 0;

        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD hh:mm:ss");
        int diffHour = currentDate.getHour() - ticket.getCheckInTime().getHour();
        int diffMinutes = currentDate.getMinute() - ticket.getCheckInTime().getMinute();

        price += diffHour * pricePerHour;
        price += diffMinutes > 0 ? 1 * pricePerHour : 0;

        return price;
    }

    @Override
    public double calculatePriceExceed(CheckInTicket ticket) {

        double price = 0;

        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD hh:mm:ss");
        int diffHour = currentDate.getHour() - ticket.getPayTime().getHour();
        int diffMinutes = currentDate.getMinute() - ticket.getPayTime().getMinute();

        price += diffHour * pricePerHour;
        price += diffMinutes > 0 ? 1 * pricePerHour : 0;

        return price;    }
}
