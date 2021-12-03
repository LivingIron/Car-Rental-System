package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import org.group29.entities.Vehicle;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;

import java.net.URL;

public class RegisterCarController {

    /*---------------Radio Buttons----------------*/
    @FXML
    private RadioButton RadioSmoking;
    @FXML
    private RadioButton RadioNonSmoking;

    /*---------------ComboBoxes----------------*/
    @FXML
    private ComboBox<VehicleClass> ClassComboBox;
    @FXML
    private ComboBox<VehicleCategory> CategoryComboBox;

    /*---------------Text Areas ----------------*/
    @FXML
    private TextArea VehicleTextArea;


    @FXML
    protected void initialize() {
        populateClassComboBox();
        populateCategoryComboBox();
    }

    public void addVehicleOnAction(){
        if(ClassComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Class!");
            alert.show();
            return;
        }
        if(CategoryComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Category!");
            alert.show();
            return;
        }
        if(VehicleTextArea.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill out the vehicle characteristics!");
            alert.show();
            return;
        }
        if(!RadioSmoking.isSelected() && !RadioNonSmoking.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select if the vehicle is for smokers or not!");
            alert.show();
            return;
        }
        boolean isForSmokers = RadioSmoking.isSelected();

        Vehicle newVehicle = new Vehicle(-1,
                ClassComboBox.getValue().getId(),
                CategoryComboBox.getValue().getId(),
                Data.operator.getFirm_id(),
                VehicleTextArea.getText(),
                isForSmokers);

        newVehicle.commit();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Car is registered!");
        alert.show();
    }

    /*-------------------------------Functions for populating ComboBoxes-----------------------------------*/

    private void populateClassComboBox(){
        VehicleClass[] strings = JavaPostgreSQL.getClassNames();
        ClassComboBox.setItems(FXCollections.observableArrayList(strings));
    }

    private void populateCategoryComboBox(){
        VehicleCategory[] strings = JavaPostgreSQL.getCategoryNames();
        CategoryComboBox.setItems(FXCollections.observableArrayList(strings));
    }

}
