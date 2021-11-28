package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.group29.entities.Firm;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;

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
        JavaPostgreSQL.addFirm(FirmNameTextField.getText());
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
        JavaPostgreSQL.addOperator(OperatorUsername.getText(),OperatorPassword.getText(),FirmComboBox.getValue().getId());
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
        if(VehicleFirmComboBox.getValue()==null){
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
        boolean isForSmokers;
        if(RadioSmoking.isSelected()){
            isForSmokers=true;
        }else{
            isForSmokers=false;
        }

        JavaPostgreSQL.addVehicle(ClassComboBox.getValue().getId(),
                                       CategoryComboBox.getValue().getId(),
                                       VehicleFirmComboBox.getValue().getId(),
                                       VehicleTextArea.getText(),
                                       isForSmokers);

    }

    public  void SwitchToFirmOnAction(){
        FirmPane.setDisable(false);
        FirmPane.setVisible(true);
        OperatorPane.setDisable(true);
        OperatorPane.setVisible(false);
        VehiclePane.setDisable(true);
        VehiclePane.setVisible(false);
    }

    public  void SwitchToOperatorOnAction(){
        populateFirmComboBox();

        FirmPane.setDisable(true);
        FirmPane.setVisible(false);
        OperatorPane.setDisable(false);
        OperatorPane.setVisible(true);
        VehiclePane.setDisable(true);
        VehiclePane.setVisible(false);
    }

    public  void SwitchToVehicleOnAction(){
        populateClassComboBox();
        populateCategoryComboBox();
        populateVehicleFirmComboBox();
        FirmPane.setDisable(true);
        FirmPane.setVisible(false);
        OperatorPane.setDisable(true);
        OperatorPane.setVisible(false);
        VehiclePane.setDisable(false);
        VehiclePane.setVisible(true);
    }

}
