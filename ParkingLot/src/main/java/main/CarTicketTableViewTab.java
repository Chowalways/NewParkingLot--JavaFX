package main;

import javafx.scene.Parent;
import javafx.scene.control.TableView;

public class CarTicketTableViewTab {

    TableView carTicket;

    public CarTicketTableViewTab (Parent parent) {
        carTicket = (TableView) parent.lookup("#CarTableView");
    }

    public void updateTableView() {

    }
}
