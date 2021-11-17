package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private TextField FirmNameTextField;
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
    private ComboBox<String> FirmComboBox;

    private void populateFirmComboBox(){
        String[] firmNames = JavaPostgreSQL.getFirmNames();
        FirmComboBox.setItems(FXCollections.observableArrayList(firmNames));
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
        FirmPane.setDisable(true);
        FirmPane.setVisible(false);
        OperatorPane.setDisable(true);
        OperatorPane.setVisible(false);
        VehiclePane.setDisable(false);
        VehiclePane.setVisible(true);
    }

}
