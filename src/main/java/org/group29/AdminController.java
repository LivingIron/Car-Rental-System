package org.group29;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {

    /*---------------Buttons----------------*/

    @FXML
    private Button AdminCloseButton;
    @FXML
    private Button HomeButton;

    /*---------------PANES----------------*/
    @FXML
    private Pane content;


    @FXML
    private void initialize(){
        HomeButton.setOnAction(e -> switchContent("AdminMenu"));
        switchContent("AdminMenu");
    }

    public void switchContent(String fileName){
        try {
            content.getChildren().clear();
            if(fileName.equals("AdminMenu")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName + ".fxml"));
                content.getChildren().add(loader.load());

                AdminMenuController controller = loader.getController();
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

    public void exitSceneOnAction(){
        Stage stage = (Stage) AdminCloseButton.getScene().getWindow();
        stage.close();
    }
}
