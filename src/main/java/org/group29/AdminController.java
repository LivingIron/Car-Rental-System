package org.group29;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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


    public void cancelButtonOnAction(){
        Stage stage = (Stage) AdminExitButton.getScene().getWindow();
        stage.close();
    }

    public void addFirmOnAction(){
        JavaPostgreSQL.AddFirm(FirmNameTextField.getText());
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
