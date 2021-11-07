package org.group29;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button exitButton;
    @FXML
    private Label loginMessageErrorLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;


    public void loginButtonOnAction(){
        loginMessageErrorLabel.setText("Logging in ...");
        JavaPostgreSQL.loginToDatabase(usernameTextField.getText(),enterPasswordField.getText());
    }

    public void cancelButtonOnAction(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
