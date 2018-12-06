package function;

public interface CheckIn {

    CheckInTicket signIn(BasicObj obj);
    boolean checkout(CheckInTicket ticket);
//    void signOut(CheckInTicket ticket);
}