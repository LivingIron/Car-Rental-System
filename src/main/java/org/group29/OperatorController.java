package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;



public class OperatorController {

    /*---------------Buttons----------------*/
    @FXML
    private Button HomeButton;
    @FXML
    private Button OperatorCloseButton;

    /*---------------PANES----------------*/
    @FXML
    private BorderPane mainPane;

    /*--------------------Labels----------------*/
    @FXML
    private Label NameText;


    public void initialize(){
        NameText.setText(Data.operatorUser);
        Data.operatorMainPane=mainPane;

        HomeButton.setOnAction(e -> FxmlLoader.switchPane(mainPane,"OperatorMenu"));
        FxmlLoader.switchPane(mainPane,"OperatorMenu");

    }

    /*--------------------------------------Functions for buttons-------------------------------------------*/

    public void exitSceneOnAction(){
        Stage stage = (Stage) OperatorCloseButton.getScene().getWindow();
        stage.close();
    }

}
