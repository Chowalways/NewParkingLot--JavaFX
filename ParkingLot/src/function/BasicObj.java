package function;

abstract class BasicObj {

    public CheckInTicket ticket;
    public String Id;

    public BasicObj(CheckInTicket ticket, String id){
        this.ticket = ticket;
        this.Id = id;
    }

    abstract void setID(String id);


}



