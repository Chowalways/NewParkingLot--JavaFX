package function;

import java.time.LocalDateTime;

public class VehincleCheckInTicket extends CheckInTicket {
    protected CheckInType type;

    public VehincleCheckInTicket(String id, LocalDateTime checkInTime, CheckInType type) {
        super(id, checkInTime);
        this.type = type;
    }

    @Override
    public CheckInTicket clone() {
        return new VehincleCheckInTicket(
                this.id,
                this.checkInTime,
                this.type
        );
    }
}
