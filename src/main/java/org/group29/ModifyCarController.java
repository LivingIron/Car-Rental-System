package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group29.entities.Condition;
import org.group29.entities.Firm;
import org.group29.entities.Vehicle;

import java.util.Optional;

public class ModifyCarController {
    @FXML
    private ComboBox<Vehicle> vehicleComboBox;
    @FXML
    private TextArea characteristicsField;
    @FXML
    private TextArea damagesField;
    @FXML
    private ComboBox<Firm> firmComboBox;

    private Vehicle selectedVehicle;
    private Condition selectedCondition;

    @FXML
    private void initialize(){
        populateVehicleComboBox();
        populateFirmComboBox();
    }

    @FXML
    public void onVehicleSelection(){
        characteristicsField.setDisable(false);
        damagesField.setDisable(false);
        firmComboBox.setDisable(false);

        selectedVehicle = vehicleComboBox.getValue();
        selectedCondition = Condition.getConditionByCar(selectedVehicle);
        Optional<Firm> foundSelected = firmComboBox.getItems().stream().filter(f -> f.getId() == selectedVehicle.getFirm_id()).findFirst();
        foundSelected.ifPresent(firm -> firmComboBox.setValue(firm));
        characteristicsField.setText(selectedVehicle.getCharacteristics());
        damagesField.setText(selectedCondition.getDamages());
    }

    @FXML
    public void onUpdateAction(){
        if(vehicleComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select operator to edit!");
            alert.show();
            return;
        }
        if(characteristicsField.getLength() == 0
                && damagesField.getLength() == 0
                && firmComboBox.getValue().getId() == selectedVehicle.getFirm_id()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please change something!");
            alert.show();
            return;
        }

        boolean vFlag = false;
        boolean cFlag = false;
        if(characteristicsField.getLength() > 0 && !characteristicsField.getText().equals(selectedVehicle.getCharacteristics())){
            selectedVehicle.setCharacteristics(characteristicsField.getText());
            vFlag = true;
        }
        if(!damagesField.getText().equals(selectedCondition.getDamages())){
            selectedCondition.setDamages(damagesField.getText());
            cFlag = true;
        }
        if(firmComboBox.getValue().getId() != selectedVehicle.getFirm_id()){
            selectedVehicle.setFirm_id(firmComboBox.getValue().getId());
            vFlag = true;
        }
        if(vFlag) selectedVehicle.commit();
        if(cFlag) selectedCondition.commit();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vehicle has been modified");
        alert.show();
    }

    private void populateVehicleComboBox(){
        Vehicle[] vehicles = JavaPostgreSQL.getVehicles();
        vehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }
    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        firmComboBox.setItems(FXCollections.observableArrayList(firms));
    }
}
