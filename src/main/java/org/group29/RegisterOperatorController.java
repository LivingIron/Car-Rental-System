package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.group29.JavaPostgreSQL;
import org.group29.entities.Firm;
import org.group29.entities.Operator;

public class RegisterOperatorController {
    @FXML
    private TextField OperatorUsername;
    @FXML
    private PasswordField OperatorPassword;
    @FXML
    private PasswordField OperatorConfirmPassword;
    @FXML
    private ComboBox<Firm> FirmComboBox;

    @FXML
    private void initialize(){
        populateFirmComboBox();
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

        Alert alert;
        if(newOperator.getId() != -1){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("New operator added!");
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Name is taken!");
        }
        alert.show();
    }

    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        FirmComboBox.setItems(FXCollections.observableArrayList(firms));
    }
}
