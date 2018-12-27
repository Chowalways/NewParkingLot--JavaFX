package main;

import CheckSystem.CheckInSystem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class SystemStatus {

    private Pane statusPane;
    private TabPane statusTab;
    private Scene statusScene;
    private Label timeLabel;
    private Label statusCarIn;
    private Label statusCarOut;
    private Label statusPersonIn;
    private Label statusPersonOut;
    //private CheckInSystem sys = new CheckInSystem();

    private static SystemStatus _instance;

    public static SystemStatus getInstance(){
        if(_instance == null){
            throw new Error("run init first");
        }else{
            return  _instance;
        }
    }

    public static void init(Parent parent){
        SystemStatus._instance = new SystemStatus(parent);

    }


    private SystemStatus(Parent parent){
        this.statusPane = (Pane) parent.lookup("#gamePane1");
        this.statusTab = (TabPane) parent.lookup("#tabPane");
        this.timeLabel  = (Label) parent.lookup("#statusTimeLabel");
        this.statusCarIn = (Label) parent.lookup("#statusCarIn");
        this.statusCarOut = (Label) parent.lookup("#statusCarOut");
        this.statusPersonIn = (Label) parent.lookup("#statusPersonIn");
        this.statusPersonOut = (Label) parent.lookup("#statusPersonOut");
        this.statusScene = parent.getScene();


        statusCarIn.setText("Cars checked-in: " + 0 );
        statusCarOut.setText("Cars checked-out: " + 0);

        statusPersonOut.setText("Workers checked-out: " + 0);


        this.statusTab.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // set Event Listener with to selected tab
                    if(newValue.getId().compareToIgnoreCase("systemStatus") == 0) {


                        if(!CheckInSystem.pInTickets.isEmpty()){
                            int count = CheckInSystem.getTicketCount("pin");
                            statusPersonIn.setText("Workers checked-in: " + count);
                        }else{
                            statusPersonIn.setText("Workers checked-in: " + 0);
                        }
                        if(!CheckInSystem.pOutTickets.isEmpty()){
                            int count = CheckInSystem.getTicketCount("pout");
                            statusPersonOut.setText("Workers checked-Out: " + count);
                        }else{
                            statusPersonOut.setText("Workers checked-Out: " + 0);
                        }
                        if(!CheckInSystem.vInTickets.isEmpty()){
                            int count = CheckInSystem.getTicketCount("vin");
                            statusCarIn.setText("Cars checked-in: " + count);
                        }else{
                            statusCarIn.setText("Cars checked-in: " + 0);
                        }
                        if(!CheckInSystem.vOutTickets.isEmpty()){
                            int count = CheckInSystem.getTicketCount("vout");
                            statusCarOut.setText("Cars checked-Out: " + count);
                        }else{
                            statusCarOut.setText("Cars checked-Out: " + 0);
                        }
                    }
                }
        );


    }




}
