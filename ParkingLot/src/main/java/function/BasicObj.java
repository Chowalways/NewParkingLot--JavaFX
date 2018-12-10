package function;

public abstract class BasicObj {

    private CheckInTicket ticket;
    private String id;

    public BasicObj(String id){
        this.id = id;
    }

    public final CheckInTicket getTicket() {
        return ticket;
    }

    public final String getId() {
        return id;
    }

    public final void setTicket(CheckInTicket ticket ) {
        this.ticket = ticket;
    }

}



