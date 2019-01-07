package Abstract;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public abstract class TableViewController {

    public TableView tableView;

    public TableViewController(TableView tableView) {
        this.tableView = tableView;
        setColumnValue();
    }

    public void setColumnValue() {
        // ID
        ((TableColumn)tableView.getColumns().get(0)).setCellValueFactory(
                new PropertyValueFactory<CheckInTicket, String>("id")
        );
        // CheckInTicket
        ((TableColumn<CheckInTicket, String>)tableView.getColumns().get(1)).setCellValueFactory(
                cellData -> cellData.getValue().getCheckInTimeProperty()
        );
        // CheckOutTicket
        ((TableColumn<CheckInTicket, String>)tableView.getColumns().get(2)).setCellValueFactory(
                cellData -> {
                    if (cellData.getValue().checkOutTime == null) {
                        return new SimpleStringProperty("");
                    }
                    return cellData.getValue().getCheckOutTimeProperty();
                }
        );
    }

    public abstract void updateTableView();
}
