package main;

import Abstract.CheckInTicket;
import Abstract.TableViewController;
import CheckSystem.CheckInSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableView;

public class WorkCheckTableViewTab extends TableViewController {


    private static WorkCheckTableViewTab _instance;

    public static WorkCheckTableViewTab getInstance() {

        if(_instance == null) {
            throw new Error("No Initial Work Check TableView Tab");
        }

        return _instance;
    }

    public static void initialize(Parent parent) {
        assert parent != null;
        _instance = new WorkCheckTableViewTab(parent);
    }

    private WorkCheckTableViewTab(Parent parent) {

        super((TableView) parent.lookup("#workTableView"));

        setColumnValue();
    }

    public void updateTableView() {
        ObservableList<CheckInTicket> data = FXCollections.observableArrayList(CheckInSystem.personCheckInTickets);
        tableView.setItems(data);
        tableView.refresh();
    }
}
