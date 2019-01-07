package main;

import Abstract.CheckInTicket;
import CheckSystem.CheckInSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class WorkCheckTableViewTab {


    private static WorkCheckTableViewTab _instance;

    public static WorkCheckTableViewTab getInstance() {

        if(_instance == null) {
            throw new Error("No Initial WOrk Check TableViewTab");
        }

        return _instance;
    }

    public static void initialize(Parent parent) {
        assert parent != null;
        _instance = new WorkCheckTableViewTab(parent);
    }

    TableView workCheck;

    private WorkCheckTableViewTab(Parent parent) {


        workCheck = (TableView) parent.lookup("#workTableView");
        AnchorPane pane = (AnchorPane) parent.lookup("#carTicketPane");
        setColumnValue();
    }

    public void setColumnValue() {
        // ID
        ((TableColumn)workCheck.getColumns().get(0)).setCellValueFactory(
                new PropertyValueFactory<CheckInTicket, String>("ID")
        );
        // CheckInTicket
        ((TableColumn)workCheck.getColumns().get(1)).setCellValueFactory(
                new PropertyValueFactory<CheckInTicket, String>("checkInTime")
        );
        // CheckOutTicket
        ((TableColumn)workCheck.getColumns().get(2)).setCellValueFactory(
                new PropertyValueFactory<CheckInTicket, String>("checkOutTime")
        );
        // Pay Ticket
//        ((TableColumn)workCheck.getColumns().get(3)).setCellValueFactory(
//                new PropertyValueFactory<CheckInTicket, String>("")
//        );
    }


    public void updateTableView() {
        ObservableList<CheckInTicket> data = FXCollections.observableArrayList(CheckInSystem.tickets);
        workCheck.getColumns();
    }
}
