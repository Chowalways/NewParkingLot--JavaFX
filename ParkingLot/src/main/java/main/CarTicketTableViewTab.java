package main;

import Abstract.CheckInTicket;
import Abstract.TableViewController;
import CheckSystem.CheckInSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableView;

public class CarTicketTableViewTab extends TableViewController {

    private static CarTicketTableViewTab _instance;

    public static void initialize(Parent parent) {
        _instance = new CarTicketTableViewTab(parent);
    }

    public static CarTicketTableViewTab getInstance() {
        if(_instance == null) {
            throw new Error("No Initial Car Ticker TableView Tab");
        }
        return _instance;
    }

    private CarTicketTableViewTab (Parent parent) {
        super((TableView)parent.lookup("#CarTableView"));
    }

    public void updateTableView() {
        ObservableList<CheckInTicket> data = FXCollections.observableArrayList(CheckInSystem.vehicleCheckInTickets);
        tableView.setItems(data);
        tableView.refresh();
    }
}
