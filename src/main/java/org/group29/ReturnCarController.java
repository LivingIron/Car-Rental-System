package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group29.entities.Condition;
import org.group29.entities.Rental;
import org.group29.entities.Return;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

public class ReturnCarController {
    @FXML
    private ComboBox<Rental> returnCarComboBox;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private TextArea returnTextArea;
    @FXML
    private TextField odometerTextField;

    private int oldOdometer;

    @FXML
    protected void initialize(){
        populateRentalComboBox();
    }

    public void enableFullForm(){
        if(returnCarComboBox.getValue()!=null){
            returnDatePicker.setDisable(false);
            returnTextArea.setDisable(false);
            odometerTextField.setDisable(false);

            Condition selectedCondition = returnCarComboBox.getValue().getCondition();
            selectedCondition.pull();

            oldOdometer = selectedCondition.getOdometer();
            odometerTextField.setText(Integer.toString(oldOdometer));
        }
    }

    public void returnCarOnAction(){
        if(returnCarComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a rental id !");
            alert.show();
            return;
        }
        if(returnDatePicker.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date!");
            alert.show();
            return;
        }
        if(returnDatePicker.getValue().isBefore(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an actual date!");
            alert.show();
            return;
        }
        if(Integer.parseInt(odometerTextField.getText()) == oldOdometer){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a new odometer reading!");
            alert.show();
            return;
        }

        Return newReturn = new Return(-1, returnCarComboBox.getValue(), Date.valueOf(returnDatePicker.getValue()));
        newReturn.commit();

        Alert alert;
        if(newReturn.getId() == -1){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date after the rent date!");
        }
        else{
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Car returned!");

            // Update vehicle condition
            Condition selectedCondition = returnCarComboBox.getValue().getCondition();
            selectedCondition.setOdometer(Integer.parseInt(odometerTextField.getText()));
            selectedCondition.addDamages(returnTextArea.getText());
            selectedCondition.commit();
        }
        alert.show();
    }


    private void populateRentalComboBox(){
        Rental[] rentals = JavaPostgreSQL.getRentals();
        rentals = Arrays.stream(rentals).filter(x -> !x.isReturned()).toArray(Rental[]::new);
        returnCarComboBox.setItems(FXCollections.observableArrayList(rentals));
    }
}
