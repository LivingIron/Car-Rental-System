package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private Button exitButton;
    @FXML
    private Label loginMessageErrorLabel;

    public void loginButtonOnAction(){
        loginMessageErrorLabel.setText("Logging in ...");
    }

    public void cancelButtonOnAction(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
