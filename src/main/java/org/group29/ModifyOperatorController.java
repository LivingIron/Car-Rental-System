package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.group29.entities.Firm;
import org.group29.entities.Operator;

import java.util.Optional;

public class ModifyOperatorController {
    @FXML
    private ComboBox<Operator> operatorComboBox;
    @FXML
    private TextField newNameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private ComboBox<Firm> firmComboBox;

    @FXML
    private void initialize(){
        populateOperatorComboBox();
        populateFirmComboBox();
    }

    @FXML
    public void onOperatorSelection(){
        newNameField.setDisable(false);
        newPasswordField.setDisable(false);
        firmComboBox.setDisable(false);
        Optional<Firm> foundSelected = firmComboBox.getItems().stream().filter(f -> f.getId() == operatorComboBox.getValue().getFirm_id()).findFirst();
        foundSelected.ifPresent(firm -> firmComboBox.setValue(firm));
    }

    @FXML
    public void onUpdateAction(){
        if(operatorComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select operator to edit!");
            alert.show();
            return;
        }

        Operator selectedOperator = operatorComboBox.getValue();

        if(newNameField.getLength() == 0 && newPasswordField.getLength() == 0 && firmComboBox.getValue().getId() == selectedOperator.getFirm_id()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter new operator username, password or firm!");
            alert.show();
            return;
        }


        if(newNameField.getLength() > 0 && !newNameField.getText().equals(selectedOperator.getUsername())){
            selectedOperator.setUsername(newNameField.getText());
        }
        if(newPasswordField.getLength() > 0 && !newPasswordField.getText().equals(selectedOperator.getPassword())){
            selectedOperator.setPassword(newPasswordField.getText());
        }
        if(firmComboBox.getValue().getId() != selectedOperator.getFirm_id()){
            selectedOperator.setFirm_id(firmComboBox.getValue().getId());
        }
        selectedOperator.commit();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Operator has been modified");
        alert.show();
    }

    private void populateOperatorComboBox(){
        Operator[] operators = JavaPostgreSQL.getOperators();
        operatorComboBox.setItems(FXCollections.observableArrayList(operators));
    }
    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        firmComboBox.setItems(FXCollections.observableArrayList(firms));
    }
}
