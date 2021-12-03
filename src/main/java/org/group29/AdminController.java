package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.group29.entities.*;

public class AdminController {

    @FXML
    private Button AdminExitButton;
    @FXML
    private Button FirmNameAddButton;
    @FXML
    private Button SwitchToFirm;
    @FXML
    private Button SwitchToOperator;
    @FXML
    private Button SwitchToVehicle;
    @FXML
    private RadioButton RadioSmoking;
    @FXML
    private RadioButton RadioNonSmoking;
    @FXML
    private TextField FirmNameTextField;
    @FXML
    private TextArea VehicleTextArea;
    @FXML
    private AnchorPane FirmPane;
    @FXML
    private AnchorPane OperatorPane;
    @FXML
    private AnchorPane VehiclePane;
    @FXML
    private TextField OperatorUsername;
    @FXML
    private PasswordField OperatorPassword;
    @FXML
    private PasswordField OperatorConfirmPassword;
    @FXML
    private TextField OperatorFirmName;
    @FXML
    private Button AddOperatorButton;
    @FXML
    private ComboBox<Firm> FirmComboBox;
    @FXML
    private ComboBox<VehicleClass> ClassComboBox;
    @FXML
    private ComboBox<VehicleCategory> CategoryComboBox;
    @FXML
    private ComboBox<Firm> VehicleFirmComboBox;

    private void disablePane(AnchorPane pane){
        pane.setDisable(true);
        pane.setVisible(false);
    }

    private void enablePane(AnchorPane pane){
        pane.setDisable(false);
        pane.setVisible(true);
    }

    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        FirmComboBox.setItems(FXCollections.observableArrayList(firms));
    }

    private void populateVehicleFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        VehicleFirmComboBox.setItems(FXCollections.observableArrayList(firms));
    }

    private void populateClassComboBox(){
        VehicleClass[] strings = JavaPostgreSQL.getClassNames();
        ClassComboBox.setItems(FXCollections.observableArrayList(strings));
    }

    private void populateCategoryComboBox(){
        VehicleCategory[] strings = JavaPostgreSQL.getCategoryNames();
        CategoryComboBox.setItems(FXCollections.observableArrayList(strings));
    }

    public void cancelButtonOnAction(){
        Stage stage = (Stage) AdminExitButton.getScene().getWindow();
        stage.close();
    }


    public void addFirmOnAction(){
        Firm newFirm = new Firm(-1, FirmNameTextField.getText());
        newFirm.commit();

        if(newFirm.getId() != -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("New firm added!");
            alert.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Name is taken!");
            alert.show();
        }
    }

    public void addOperatorOnAction(){
        if(!OperatorPassword.getText().equals(OperatorConfirmPassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Passwords dont match!");
            alert.show();
            return;
        }
        if(FirmComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a firm!");
            alert.show();
            return;
        }

        Operator newOperator = new Operator(-1,
                FirmComboBox.getValue().getId(),
                OperatorUsername.getText(),
                OperatorPassword.getText());

        newOperator.commit();

        if(newOperator.getId() != -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("New operator added!");
            alert.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Name is taken!");
            alert.show();
        }
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


    public void SwitchToFirmOnAction(){
        disablePane(OperatorPane);
        disablePane(VehiclePane);
        enablePane(FirmPane);
    }

    public void SwitchToOperatorOnAction(){
        populateFirmComboBox();

        disablePane(FirmPane);
        disablePane(VehiclePane);
        enablePane(OperatorPane);
    }

    public void SwitchToVehicleOnAction(){
        populateClassComboBox();
        populateCategoryComboBox();
        populateVehicleFirmComboBox();

        disablePane(OperatorPane);
        disablePane(FirmPane);
        enablePane(VehiclePane);
    }
}
