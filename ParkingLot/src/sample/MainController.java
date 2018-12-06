package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    Label lblTime;

    public void up(ActionEvent event) {
        lblTime.setText("Hello, World.");
        System.out.println("Up");
    }

    public void down(ActionEvent event) {
        System.out.println("Down");
    }

    public void left(ActionEvent event) {
        System.out.println("Left");
    }

    public void right(ActionEvent event) {
        System.out.println("Right");
    }
}
