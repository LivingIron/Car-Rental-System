package org.group29;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.group29.entities.Client;

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
        }
        else{
            Client newClient = new Client(-1,
                    Data.operator.getFirm_id(),
                    5,
                    clientNameTextField.getText(),
                    clientPhoneTextField.getText());

            newClient.commit();

            Alert alert;
            if(newClient.getId() != -1){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("New client registered!");
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Client already exists!");
            }
            alert.show();
        }
    }
}
