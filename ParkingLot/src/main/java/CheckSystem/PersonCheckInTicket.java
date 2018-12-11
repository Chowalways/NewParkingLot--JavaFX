package CheckSystem;

import Abstract.CheckInTicket;

import java.time.LocalDateTime;

public class PersonCheckInTicket extends CheckInTicket {

    public PersonCheckInTicket(String id, LocalDateTime checkInTime) {
        super(id, checkInTime);
    }

    @Override
    public CheckInTicket clone() {
        return new PersonCheckInTicket(
                this.id,
                this.checkInTime
        );
    }
}
