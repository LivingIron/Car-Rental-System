package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Arrays;

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
    private Button AddOperatorButton;
    @FXML
    private RadioButton RadioSmoking;
    @FXML
    private RadioButton RadioNonSmoking;
    @FXML
    private TextField FirmNameTextField;
    @FXML
    private TextField OperatorFirmName;
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
    private ComboBox<String> FirmComboBox;
    @FXML
    private ComboBox<String> ClassComboBox;
    @FXML
    private ComboBox<String> CategoryComboBox;
    @FXML
    private ComboBox<String> VehicleFirmComboBox;

    private void populateFirmComboBox(){
        String[] firmNames = JavaPostgreSQL.getFirmNames();
        FirmComboBox.setItems(FXCollections.observableArrayList(firmNames));
    }
    private void populateClassComboBox(){
        String[] classNames = JavaPostgreSQL.getClassNames();
        System.out.println(Arrays.toString(classNames));
        ClassComboBox.setItems(FXCollections.observableArrayList(classNames));
    }
    private void populateCategoryComboBox(){
        String[] categoryNames = JavaPostgreSQL.getCategoryNames();
        System.out.println(Arrays.toString(categoryNames));
        CategoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
    }
    private void populateVehicleFirmComboBox(){
        String[] vehicleFirmNames = JavaPostgreSQL.getFirmNames();
        System.out.println(Arrays.toString(vehicleFirmNames));
        VehicleFirmComboBox.setItems(FXCollections.observableArrayList(vehicleFirmNames));
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
        JavaPostgreSQL.addOperator(OperatorUsername.getText(),OperatorPassword.getText(),FirmComboBox.getValue());
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
        populateCategoryComboBox();
        //populateClassComboBox();
        //populateVehicleFirmComboBox();
        FirmPane.setDisable(true);
        FirmPane.setVisible(false);
        OperatorPane.setDisable(true);
        OperatorPane.setVisible(false);
        VehiclePane.setDisable(false);
        VehiclePane.setVisible(true);
    }

}
