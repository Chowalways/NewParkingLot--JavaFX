package Class;

import Abstract.BasicObj;
import CheckSystem.Other.Gender;

public class Person extends BasicObj {

    private Gender gender;
    private int age;

    public Person(String id, Gender gender, int age){
        super(id);
        this.gender = gender;
        this.age = age;
    }
}
