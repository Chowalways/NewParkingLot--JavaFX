package function;

import function.BasicObj;

public class Vehicle extends BasicObj {

    private int wheelNo;
    private double weight;

    public Vehicle(CheckInTicket ticket, String id, int wheelNo, double weight) {
        super(ticket, id);
        this.wheelNo = wheelNo;
        this.weight = weight;
    }

    public void setID(String id){
        super.Id = id;
    }

}
