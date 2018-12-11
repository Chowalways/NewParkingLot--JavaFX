package Unit;


import function.CheckInTicket;
import function.Vehicle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.GameObject;

public class Car extends GameObject {

    Vehicle vehicle;

    private Car(ImageView image, String id, int numberOfWheels, double weight) {
        super(image);
        vehicle = new Vehicle(id, numberOfWheels, weight);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public static Car createCar(String id, int numberOfWheels, double weight) {
        ImageView imageView = new ImageView();
        Image image = new Image("res/images/car.png");
        imageView.setImage(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(20);

        return new Car(imageView, id, numberOfWheels, weight);
    }

    public final void checkIn(CheckInTicket ticket) {
        vehicle.setTicket(ticket);
    }

    public final CheckInTicket getTicket() {
        return vehicle.getTicket();
    }

    public final boolean hasTicket() {
        return vehicle.hasTicket();
    }

    public final void removeTicket() {
        this.vehicle.setTicket(null);
    }
}
