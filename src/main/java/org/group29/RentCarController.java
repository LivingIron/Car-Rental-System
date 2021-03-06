package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group29.entities.Client;
import org.group29.entities.Condition;
import org.group29.entities.Rental;
import org.group29.entities.Vehicle;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

public class RentCarController {

    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private ComboBox<Vehicle> carComboBox;
    @FXML
    private TextField odometerTextField;
    @FXML
    private TextField durationTextField;
    @FXML
    private TextArea conditionTextArea;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private Label riskLabel;

    @FXML
    protected void initialize(){
        populateVehiclesComboBox();
        populateClientComboBox();
    }

    public void checkRisk(){
        Client selectedClient = clientComboBox.getValue();
        riskLabel.setVisible(selectedClient.getRating() <= 3);
    }

    public void enableFullForm(){
        odometerTextField.setDisable(false);
        durationTextField.setDisable(false);
        conditionTextArea.setDisable(false);
        dateDatePicker.setDisable(false);

        Condition selectedCondition = Condition.getConditionByCar(carComboBox.getValue());

        conditionTextArea.setText(selectedCondition.getDamages());
        odometerTextField.setText(Integer.toString(selectedCondition.getOdometer()));
    }

    public void rentButtonOnAction(){
        if(carComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Car!");
            alert.show();
            return;
        }
        if(clientComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Client!");
            alert.show();
            return;
        }
        if(dateDatePicker.getValue() == null){
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
            alert.setContentText("Please enter a duration!");
            alert.show();
            return;
        }
        if(Integer.parseInt(durationTextField.getText())<=0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Select a duration larger than 0!");
            alert.show();
            return;
        }

        Condition carCondition = Condition.getConditionByCar(carComboBox.getValue());

        Rental newRental = new Rental(-1,
                Data.operator.getFirm_id(),
                carComboBox.getValue(),
                clientComboBox.getValue(),
                carCondition,
                Integer.parseInt(durationTextField.getText()),
                Date.valueOf(dateDatePicker.getValue()),
                false);

        newRental.commit();

        Alert alert;
        if(newRental.getId() == -1){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Rent days for this car are overlapping!");
        }
        else{
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Car rented!");
        }
        alert.show();
    }

    private void populateVehiclesComboBox(){
        Vehicle[] vehicles = JavaPostgreSQL.getVehicles();
        vehicles = Arrays.stream(vehicles).filter(v -> v.getFirm_id() == Data.operator.getFirm_id()).toArray(Vehicle[]::new);
        carComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }
    private void populateClientComboBox(){
        Client[] clients = JavaPostgreSQL.getClients();
        clients = Arrays.stream(clients).filter(c -> c.getFirm_id() == Data.operator.getFirm_id()).toArray(Client[]::new);
        clientComboBox.setItems(FXCollections.observableArrayList(clients));
    }
}
