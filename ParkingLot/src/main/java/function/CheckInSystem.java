package function;

public class CheckInSystem {

    protected CheckIn checkIn;
    protected int count = 1;
    private final int lotTotal = 100; //test Value
    //public ArrayList<CheckInTicket> ticketHistory = new ArrayList<CheckInTicket>();

    public CheckInSystem(){

    }

    public boolean checkAvailable(){
        if (count == lotTotal){
            return false;
        }else{
            return true;
        }
    }



}
