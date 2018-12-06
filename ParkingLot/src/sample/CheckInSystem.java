package sample;

import java.util.ArrayList;
import java.util.List;

public class CheckInSystem {

    public CheckIn checkIn;
    public int count;
    public final int lotTotal = 100; //test Value
    //public ArrayList<CheckInTicket> ticketHistory = new ArrayList<CheckInTicket>();

    public CheckInSystem(BasicObj obj){
        if(checkAvailable())
            checkIn.signIn(obj);
        else
            System.out.println("No Space Available");
    }

    public boolean checkAvailable(){
        if (count == lotTotal){
            return false;
        }else{
            return true;
        }
    }



}
