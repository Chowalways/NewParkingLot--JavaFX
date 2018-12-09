package Unit;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.GameObject;

public class Car extends GameObject {

    private Car(ImageView image) {
        super(image);
    }

    public static Car createCar() {
        ImageView imageView = new ImageView();
        Image image = new Image("res/images/car.png");
        imageView.setImage(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(20);

        return new Car(imageView);
    }
}
