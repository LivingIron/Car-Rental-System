package org.group29;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RegisterClientController {

    @FXML
    private TextField clientNameTextField;
    @FXML
    private TextField clientPhoneTextField;

    public void addClientOnAction(){
        if(clientPhoneTextField.getText().equals("")||clientNameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the empty fields!");
            alert.show();
        }else{
            JavaPostgreSQL.addClient(clientNameTextField.getText(),clientPhoneTextField.getText());
        }
    }
}
