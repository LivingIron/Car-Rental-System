package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import org.group29.Data;
import org.group29.JavaPostgreSQL;
import org.group29.entities.Firm;
import org.group29.entities.Vehicle;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;

public class AdminRegisterCarController {

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
    @FXML
    private ComboBox<Firm> VehicleFirmComboBox;

    /*---------------Text Areas ----------------*/
    @FXML
    private TextArea VehicleTextArea;


    @FXML
    protected void initialize() {
        populateClassComboBox();
        populateCategoryComboBox();
        populateFirmComboBox();
    }

    public void addVehicleOnAction(){
        if(ClassComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Class!");
            alert.show();
            return;
        }
        if(CategoryComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Category!");
            alert.show();
            return;
        }
        if(VehicleFirmComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a firm!");
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
                VehicleFirmComboBox.getValue().getId(),
                VehicleTextArea.getText(),
                isForSmokers);

        newVehicle.commit();

        if(newVehicle.getId() != -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("New vehicle added!");
            alert.show();
        }
    }

    /*-------------------------------Functions for populating ComboBoxes-----------------------------------*/

    private void populateClassComboBox(){
        VehicleClass[] vehicles = JavaPostgreSQL.getClassNames();
        ClassComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }

    private void populateCategoryComboBox(){
        VehicleCategory[] categories = JavaPostgreSQL.getCategoryNames();
        CategoryComboBox.setItems(FXCollections.observableArrayList(categories));
    }

    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        VehicleFirmComboBox.setItems(FXCollections.observableArrayList(firms));
    }

}
