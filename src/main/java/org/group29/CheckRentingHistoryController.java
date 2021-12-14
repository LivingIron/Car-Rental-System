package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import org.group29.entities.Rental;
import org.group29.entities.Vehicle;

import java.sql.Date;
import java.util.Arrays;

public class CheckRentingHistoryController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<Vehicle> vehicleComboBox;
    @FXML
    private ListView<Rental> rentalList;

    @FXML
    private void initialize(){
        populateVehicleComboBox();
    }

    @FXML
    private void checkButtonAction(){
        if(vehicleComboBox.getValue() == null){
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

        Vehicle selectedVehicle = vehicleComboBox.getValue();
        Rental[] rentals = JavaPostgreSQL.getRentalsByVehicle(selectedVehicle, Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()));
        rentalList.setItems(FXCollections.observableArrayList(rentals));
    }

    private void populateVehicleComboBox(){
        Vehicle[] vehicles = JavaPostgreSQL.getVehicles();
        vehicles = Arrays.stream(vehicles).filter(v -> v.getFirm_id() == Data.operator.getFirm_id()).toArray(Vehicle[]::new);
        vehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }
}
