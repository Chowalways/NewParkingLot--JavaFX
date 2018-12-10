package function;

public class Person extends BasicObj {

    private Gender gender;
    private int age;

    public Person(CheckInTicket ticket, String id, Gender gender, int age){
        super(id);
        this.gender = gender;
        this.age = age;
    }

}
