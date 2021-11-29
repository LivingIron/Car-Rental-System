package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group29.entities.Client;
import org.group29.entities.VehicleClass;
import org.group29.entities.Vehicles;

import java.sql.Date;
import java.time.LocalDate;

public class RentCarController {

    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private ComboBox<Vehicles> carComboBox;
    @FXML
    private TextField odometerTextField;
    @FXML
    private TextField durationTextField;
    @FXML
    private TextArea conditionTextArea;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private Button rentButton;

    @FXML
    protected void initialize(){
        JavaPostgreSQL.checkForRentDays(); // updates rent days
        populateVehiclesComboBox();
        populateClientComboBox();
    }

    public void enableFullForm(){

        if(clientComboBox.getValue()!=null&&carComboBox.getValue()!=null){
            odometerTextField.setDisable(false);
            durationTextField.setDisable(false);
            conditionTextArea.setDisable(false);
            dateDatePicker.setDisable(false);

            conditionTextArea.setText(JavaPostgreSQL.getCarCondition(carComboBox.getValue().getId()));
            odometerTextField.setText( JavaPostgreSQL.getCarOdometer(carComboBox.getValue().getId()));


        }
    }

    public void rentButtonOnAction(){
        if(carComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Car!");
            alert.show();
            return;
        }
        if(clientComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Client!");
            alert.show();
            return;
        }
        if(dateDatePicker.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Date!");
            alert.show();
            return;
        }
        if(dateDatePicker.getValue().isBefore(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date in the present or future!");
            alert.show();
            return;
        }
        if(durationTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a duration !");
            alert.show();
            return;
        }
        if(Integer.parseInt(durationTextField.getText())<=0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Select a duration bigger than 0 !");
            alert.show();
            return;
        }

        System.out.println(clientComboBox.getValue().getId());
        JavaPostgreSQL.rentCar( Integer.parseInt(carComboBox.getValue().toString()),
                                clientComboBox.getValue().getId(),
                                JavaPostgreSQL.carIdToConditionId(Integer.parseInt(carComboBox.getValue().toString())),
                                Date.valueOf(dateDatePicker.getValue()),
                                Integer.parseInt(durationTextField.getText()),
                                Data.operatorId);

    }



    private void populateVehiclesComboBox(){
        Vehicles[] strings = JavaPostgreSQL.getVehicles();
        carComboBox.setItems(FXCollections.observableArrayList(strings));
    }
    private void populateClientComboBox(){
        Client[] strings = JavaPostgreSQL.getClients();
        clientComboBox.setItems(FXCollections.observableArrayList(strings));
    }

}
