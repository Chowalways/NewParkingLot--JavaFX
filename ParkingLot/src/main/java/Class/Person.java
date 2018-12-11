package Class;

import Abstract.BasicObj;
import Abstract.CheckInTicket;
import CheckSystem.Other.Gender;

public class Person extends BasicObj {

    private Gender gender;
    private int age;

    public Person(CheckInTicket ticket, String id, Gender gender, int age){
        super(id);
        this.gender = gender;
        this.age = age;
    }

}
