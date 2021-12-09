package org.group29;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.group29.entities.Firm;

public class RegisterFirmController {
    @FXML
    private TextField FirmNameTextField;

    public void addFirmOnAction(){
        Firm newFirm = new Firm(-1, FirmNameTextField.getText());
        newFirm.commit();

        Alert alert;
        if(newFirm.getId() != -1){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("New firm added!");
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Name is taken!");
        }
        alert.show();
    }
}
