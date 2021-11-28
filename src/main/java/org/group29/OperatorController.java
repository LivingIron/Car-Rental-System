package org.group29;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.group29.LoginController;

import java.io.IOException;
import java.util.*;


public class OperatorController {

    /*---------------Buttons----------------*/
    @FXML
    private Button OperatorCloseButton;
    @FXML
    private Button RegisterCarButton;
    @FXML
    private Button RegisterClientButton;
    @FXML
    private Button RentCarButton;
    @FXML
    private Button ReturnCarButton;
    @FXML
    private Button CalculatePriceButton;
    @FXML
    private Button CheckAvailableCarsButton;
    @FXML
    private Button CheckRentingHistoryButton;
    @FXML
    private Button CheckOperatorHistoryButton;
    @FXML
    private Button CheckClientRatingsButton;
    @FXML
    private Button CheckStatsOfCarsButton;

    /*---------------PANES----------------*/
    @FXML
    private AnchorPane RegisterCarPane;
    @FXML
    private AnchorPane RegisterClientPane;
    @FXML
    private AnchorPane RentCarPane;
    @FXML
    private AnchorPane ReturnCarPane;
    @FXML
    private AnchorPane CalculatePricePane;
    @FXML
    private AnchorPane CheckAvailableCarsPane;
    @FXML
    private AnchorPane CheckRentingHistoryPane;
    @FXML
    private AnchorPane CheckOperatorHistoryPane;
    @FXML
    private AnchorPane CheckClientRatingsPane;
    @FXML
    private AnchorPane CheckStatsOfCarsPane;
    @FXML
    private AnchorPane MainMenuPane;
    /*---------------Text fields----------------*/
    @FXML
    private TextField clientNameTextField;
    @FXML
    private TextField clientPhoneTextField;


    @FXML
    private Label NameText;


    private Vector<AnchorPane> AllPanes = new Vector<>();

    public void initialize(){
        NameText.setText(Data.operatorUser);
        AllPanes.add(RegisterCarPane);
        AllPanes.add(RegisterClientPane);
        AllPanes.add(RentCarPane);
        AllPanes.add(ReturnCarPane);
        AllPanes.add(CalculatePricePane);
        AllPanes.add(CheckAvailableCarsPane);
        AllPanes.add(CheckRentingHistoryPane);
        AllPanes.add(CheckOperatorHistoryPane);
        AllPanes.add(CheckClientRatingsPane);
        AllPanes.add(CheckStatsOfCarsPane);
        AllPanes.add(MainMenuPane);
        switchToMainMenu();
    }

    public void exitSceneOnAction(){
        Stage stage = (Stage) OperatorCloseButton.getScene().getWindow();
        stage.close();
    }

    public void addClientOnAction(){
        if(clientPhoneTextField.getText().equals("")||clientNameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the empty fields!");
            alert.show();
            return;
        }else{
            JavaPostgreSQL.addClient(clientNameTextField.getText(),clientPhoneTextField.getText());
        }
    }
    /*-------------------------------Functions for switching between panes-----------------------------------*/



    public void disableAllPanes(){
        for(AnchorPane temp : AllPanes){
            temp.setDisable(true);
            temp.setVisible(false);
        }
    }

    public void activatePane(AnchorPane temp){
        temp.setVisible(true);
        temp.setDisable(false);
    }

    public void switchToMainMenu(){
        disableAllPanes();
        activatePane(MainMenuPane);
    }

    public void switchToRegisterCar(){
        disableAllPanes();
        activatePane(RegisterCarPane);
    }

    public void switchToRegisterClient(){
        disableAllPanes();
        activatePane(RegisterClientPane);
    }

    public void switchToRentCar(){
        disableAllPanes();
        activatePane(RentCarPane);
    }

    public void switchToReturnCar(){
        disableAllPanes();
        activatePane(ReturnCarPane);
    }

    public void switchToCalculatePrice(){
        disableAllPanes();
        activatePane(CalculatePricePane);
    }

    public void switchToAvailableCars(){
        disableAllPanes();
        activatePane(CheckAvailableCarsPane);
    }

    public void switchToRentingHistory(){
        disableAllPanes();
        activatePane(CheckRentingHistoryPane);
    }

    public void switchToOperatorHistory(){
        disableAllPanes();
        activatePane(CheckOperatorHistoryPane);
    }

    public void switchToClientRatings(){
        disableAllPanes();
        activatePane(CheckClientRatingsPane);
    }

    public void switchToCarStats(){
        disableAllPanes();
        activatePane(CheckStatsOfCarsPane);
    }


}
