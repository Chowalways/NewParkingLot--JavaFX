package function;

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
        if(currentDate.getMinute() - ticket.payTime.getMinute()  > 15 ) {
            return CheckInStatus.TimeExceed;
        }

        return CheckInStatus.Done;
    }

    @Override
    public double calculatePrice(CheckInTicket ticket) {

        double price = 0;

        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD hh:mm:ss");
        int diffHour = currentDate.getHour() - ticket.checkInTime.getHour();
        int diffMinutes = currentDate.getMinute() - ticket.checkInTime.getMinute();

        price += diffHour * pricePerHour;
        price += diffMinutes > 0 ? 1 * pricePerHour : 0;

        return price;
    }

    @Override
    public double calculatePriceExceed(CheckInTicket ticket) {

        double price = 0;

        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(ConfigManager.getString("TIMEZONE")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD hh:mm:ss");
        int diffHour = currentDate.getHour() - ticket.payTime.getHour();
        int diffMinutes = currentDate.getMinute() - ticket.payTime.getMinute();

        price += diffHour * pricePerHour;
        price += diffMinutes > 0 ? 1 * pricePerHour : 0;

        return price;    }
}
