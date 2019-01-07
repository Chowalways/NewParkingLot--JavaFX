package Unit;

import Abstract.CheckInTicket;
import Abstract.GameObject;
import Class.Person;
import CheckSystem.Other.Gender;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PersonObject extends GameObject {

    static Person person;

    private PersonObject(ImageView image, String id, Gender gender, int age){
        super(image);
        person = new Person(id, gender, age);
    }

    public Person getPerson() {
        return this.person;
    }

    public static PersonObject createPerson(String id, Gender gender, int age) {
        person = new Person(id, gender, age);
        ImageView iViewer = new ImageView();
        Image image = new Image("res/images/person.png");
        iViewer.setImage(image);
        iViewer.setFitWidth(20);
        iViewer.setFitHeight(25);
        return new PersonObject(iViewer, id, gender, age);
    }

    public final void checkIn(CheckInTicket ticket) {
        person.setTicket(ticket);
    }

    public final CheckInTicket getTicket() {
        return person.getTicket();
    }

    public final boolean hasTicket() {
        return person.hasTicket();
    }
}
