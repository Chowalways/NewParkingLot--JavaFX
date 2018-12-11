package function;

import java.time.LocalDateTime;

public class VehicleCheckInTicket extends CheckInTicket {
    protected CheckInType type;

    public VehicleCheckInTicket(String id, LocalDateTime checkInTime, CheckInType type) {
        super(id, checkInTime);
        this.type = type;
    }

    @Override
    public CheckInTicket clone() {
        return new VehicleCheckInTicket(
                this.id,
                this.checkInTime,
                this.type
        );
    }
}
