package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import org.group29.entities.Vehicle;

import java.time.LocalDate;
import java.sql.Date;

public class CheckAvailableCarsController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ListView<Vehicle> vehicleList;

    @FXML
    private void checkButtonAction(){
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date!");
            alert.show();
            return;
        }
        if(startDatePicker.getValue().isBefore(LocalDate.now()) || endDatePicker.getValue().isBefore(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an actual date!");
            alert.show();
            return;
        }
        if(endDatePicker.getValue().isBefore(startDatePicker.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("End date is before start date!");
            alert.show();
            return;
        }

        Date startDate = Date.valueOf(startDatePicker.getValue());
        Date endDate = Date.valueOf(endDatePicker.getValue());

        Vehicle[] cars = JavaPostgreSQL.getAvailableVehicles(startDate, endDate);
        vehicleList.setItems(FXCollections.observableArrayList(cars));
    }
}
