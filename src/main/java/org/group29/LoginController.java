package org.group29;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.group29.entities.Operator;

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
        int result = JavaPostgreSQL.loginToDatabase(usernameTextField.getText(),enterPasswordField.getText());
        if(result == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Provided Credentials are incorrect");
            alert.show();
        }
        else if(result == 0){
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
        }
        else {
            Data.operator = new Operator(result);
            Data.operator.pull();

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OperatorLogged.fxml"));
            Pane myPane = loader.load();
            Scene scene = new Scene(myPane);

            stage.setTitle("Operator Panel");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            closeStage();
            stage.setOnShown(e -> ((OperatorController) loader.getController()).checkExpiry());
            stage.show();
        }
    }

    @FXML
    private void cancelButtonOnAction(){
        closeStage();
    }
}
