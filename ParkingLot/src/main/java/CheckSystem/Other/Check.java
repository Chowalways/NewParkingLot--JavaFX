package CheckSystem.Other;

import Abstract.BasicObj;
import Abstract.CheckInTicket;

public interface Check {

    // Because we need to generate id for Ticket so need to pass id from system.
    CheckInTicket signIn(BasicObj obj, String id);
    boolean checkout(CheckInTicket ticket);
    CheckInStatus validate(CheckInTicket ticket);
    double calculatePrice(CheckInTicket ticket);
    double calculatePriceExceed(CheckInTicket ticket);

}