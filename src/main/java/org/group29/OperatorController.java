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
import org.group29.LoginController;

import java.io.IOException;


public class OperatorController {

    @FXML
    private Button OperatorCloseButton;

    public void exitSceneOnAction(){
        Stage stage = (Stage) OperatorCloseButton.getScene().getWindow();
        stage.close();
    }

}
