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
    private Label loginMessageErrorLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;

    public void closeStage()
    {
        ((Stage) usernameTextField.getScene().getWindow()).close();
    }

    @FXML
    private void login() throws IOException {
        loginMessageErrorLabel.setText("Logging in ...");
        if(JavaPostgreSQL.loginToDatabase(usernameTextField.getText(),enterPasswordField.getText())) {

            if(usernameTextField.getText().equals("admin")){
                Stage stage = new Stage();
                Pane myPane;
                myPane = FXMLLoader.load(getClass().getResource("AdminLogged.fxml"));
                Scene scene = new Scene(myPane);

                stage.setTitle("Admin Panel");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);

                closeStage();
                stage.show();
            }else{
                Stage stage = new Stage();
                Pane myPane;
                myPane = FXMLLoader.load(getClass().getResource("OperatorLogged.fxml"));
                Scene scene = new Scene(myPane);

                stage.setTitle("Operator Panel");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);

                Data.operatorUser=usernameTextField.getText();
                Data.operatorPass=enterPasswordField.getText();
                Data.operatorId=JavaPostgreSQL.getFirmId(usernameTextField.getText());

                closeStage();
                stage.show();
            }
        }
    }

    @FXML
    private void cancelButtonOnAction(){
        closeStage();
    }
}
