package Abstract;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public abstract class CheckInTicket {

    protected String id;
    protected LocalDateTime checkInTime;
    protected LocalDateTime checkOutTime = null;
    protected LocalDateTime payTime = null;

    public CheckInTicket(String id, LocalDateTime checkInTime) {
        this.id = id;
        this.checkInTime = checkInTime;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public StringProperty getCheckInTimeProperty() { return new SimpleStringProperty(checkInTime.toString().replaceFirst("T", " "));}

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public StringProperty getCheckOutTimeProperty() { return new SimpleStringProperty(checkOutTime.toString().replaceFirst("T", " "));}

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public boolean isPaid() {
        return this.payTime != null;
    }

    public void checkOut(LocalDateTime time) {
        this.checkOutTime = time;
    }

    public abstract CheckInTicket clone();

}
