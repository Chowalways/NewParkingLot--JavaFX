package function;

public abstract class BasicObj {

    protected CheckInTicket ticket;
    protected String Id;

    public BasicObj(CheckInTicket ticket, String id){
        this.ticket = ticket;
        this.Id = id;
    }

}



