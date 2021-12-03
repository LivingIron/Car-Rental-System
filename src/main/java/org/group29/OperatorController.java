package org.group29;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OperatorController {

    /*---------------Buttons----------------*/
    @FXML
    private Button HomeButton;
    @FXML
    private Button OperatorCloseButton;

    /*---------------PANES----------------*/
    @FXML
    public Pane content;

    /*--------------------Labels----------------*/
    @FXML
    private Label NameText;


    public void initialize(){
        NameText.setText(Data.operator.getUsername());

        HomeButton.setOnAction(e -> switchContent("OperatorMenu"));
        switchContent("OperatorMenu");
    }

    public void switchContent(String fileName){
        try {
            content.getChildren().clear();
            if(fileName.equals("OperatorMenu")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName + ".fxml"));
                content.getChildren().add(loader.load());

                OperatorMenuController controller = loader.getController();
                controller.valueProperty().addListener((observable, oldValue, newValue) -> switchContent(newValue));
            }
            else{
                content.getChildren().add(FXMLLoader.load(getClass().getResource(fileName + ".fxml")));
            }
        }
        catch(IOException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /*--------------------------------------Functions for buttons-------------------------------------------*/

    public void exitSceneOnAction(){
        Stage stage = (Stage) OperatorCloseButton.getScene().getWindow();
        stage.close();
    }

}
