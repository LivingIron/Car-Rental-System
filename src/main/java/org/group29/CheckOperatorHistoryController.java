package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import org.group29.entities.HistoryEntry;
import org.group29.entities.Operator;

import java.sql.Date;
import java.util.Arrays;

public class CheckOperatorHistoryController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<Operator> operatorComboBox;
    @FXML
    private ListView<HistoryEntry> historyList;

    @FXML
    private void initialize(){
        populateOperatorComboBox();
    }

    @FXML
    private void checkButtonAction(){
        if(operatorComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a vehicle!");
            alert.show();
            return;
        }
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date!");
            alert.show();
            return;
        }
        if(endDatePicker.getValue().isBefore(startDatePicker.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("End date is before start date!");
            alert.show();
            return;
        }

        Operator selectedOperator = operatorComboBox.getValue();
        HistoryEntry[] entries = JavaPostgreSQL.getOperatorHistory(selectedOperator, Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()));
        Arrays.sort(entries);
        historyList.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateOperatorComboBox(){
        Operator[] operators = JavaPostgreSQL.getOperators();
        operatorComboBox.setItems(FXCollections.observableArrayList(operators));
    }
}
