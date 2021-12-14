package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.group29.entities.Firm;

public class ModifyFirmController {
    @FXML
    private ComboBox<Firm> firmComboBox;
    @FXML
    private TextField newNameField;

    @FXML
    private void initialize(){
        populateFirmComboBox();
    }

    @FXML
    public void onFirmSelection(){
        newNameField.setDisable(false);
    }

    @FXML
    public void onUpdateAction(){
        if(firmComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select firm to edit!");
            alert.show();
            return;
        }
        if(newNameField.getLength() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter new firm name!");
            alert.show();
            return;
        }

        Firm selectedFirm = firmComboBox.getValue();
        selectedFirm.setName(newNameField.getText());
        selectedFirm.commit();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Firm name has been changed");
        alert.show();
    }

    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        firmComboBox.setItems(FXCollections.observableArrayList(firms));
    }
}
