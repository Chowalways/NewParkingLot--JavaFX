package sample;

public interface CheckIn {

    CheckInTicket signIn(BasicObj obj);
    void signOut(CheckInTicket ticket);
}