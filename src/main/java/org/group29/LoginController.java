package org.group29;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button exitButton;
    @FXML
    private Label loginMessageErrorLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;

    private void closeStage()
    {
        ((Stage) usernameTextField.getScene().getWindow()).close();
    }

    public void loginButtonOnAction() throws IOException {
        loginMessageErrorLabel.setText("Logging in ...");
        if(JavaPostgreSQL.loginToDatabase(usernameTextField.getText(),enterPasswordField.getText())) {
            Stage stage = new Stage();
            stage.setTitle("Admin Panel");
            Pane myPane;
            myPane = FXMLLoader.load(getClass().getResource("AdminLogged.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);

            closeStage();

            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
    }

    public void cancelButtonOnAction(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
