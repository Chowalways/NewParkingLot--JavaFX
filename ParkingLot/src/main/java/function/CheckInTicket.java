package function;

import java.time.LocalDateTime;

public abstract class CheckInTicket {

    protected String id;
    protected LocalDateTime checkInTime;
    protected LocalDateTime checkOutTime = null;
    protected LocalDateTime payDate = null;

    public CheckInTicket(String id, LocalDateTime checkInTime) {
        this.id = id;
        this.checkInTime = checkInTime;
    }

    public boolean isPaid() {
        return this.payDate != null;
    }

    public void checkOut(LocalDateTime time) {
        this.checkOutTime = time;
    }

    public abstract CheckInTicket clone();

}
