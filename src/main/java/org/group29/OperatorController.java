package org.group29;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.group29.entities.Rental;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
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

    @FXML
    public void initialize(){
        NameText.setText(Data.operator.getUsername());

        HomeButton.setOnAction(e -> switchContent("OperatorMenu"));
        switchContent("OperatorMenu");
    }

    public void checkExpiry(){
        Rental[] alertRentals = Arrays.stream(JavaPostgreSQL.getRentals())
                .filter(r -> !r.isReturned()
                        &&
                        (LocalDate.now().equals(r.getRental_date().toLocalDate().plusDays(r.getDuration()))
                        || LocalDate.now().isAfter(r.getRental_date().toLocalDate().plusDays(r.getDuration()))))
                .toArray(Rental[]::new);
        if(alertRentals.length == 0) return;

        Alert expiryAlert = new Alert(Alert.AlertType.WARNING);
        expiryAlert.setHeaderText("Expired/expiring rentals");
        expiryAlert.initStyle(StageStyle.UNDECORATED);
        expiryAlert.initModality(Modality.WINDOW_MODAL);

        VBox alertList = new VBox();
        ScrollPane alertScroll = new ScrollPane(alertList);
        expiryAlert.getDialogPane().setContent(alertScroll);

        for(Rental r : alertRentals){
            r.getClient().pull();
            if(LocalDate.now().isAfter(r.getRental_date().toLocalDate().plusDays(r.getDuration()))){
                Label label = new Label(String.format("EXPIRED! #%d Client (%s) phone number: %s", r.getId(), r.getClient().getName(), r.getClient().getPhone()));
                label.setTextFill(Color.RED);
                alertList.getChildren().add(label);
            }
            else alertList.getChildren().add(new Label(String.format("#%d Client (%s) phone number: %s", r.getId(), r.getClient().getName(), r.getClient().getPhone())));
        }

        Window currWindow = HomeButton.getScene().getWindow();
        expiryAlert.setX(currWindow.getX() + currWindow.getWidth());
        expiryAlert.setY(currWindow.getY());

        expiryAlert.show();
        expiryAlert.setHeight(Math.min(currWindow.getHeight(), expiryAlert.getHeight()));
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
