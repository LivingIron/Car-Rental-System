package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;

import java.util.Arrays;
import java.util.Vector;


public class OperatorController {

    /*---------------Buttons----------------*/
    @FXML
    private Button HomeButton;
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
    @FXML
    private Button AddVehicleButton;

    /*---------------Radio Buttons----------------*/
    @FXML
    private RadioButton RadioSmoking;
    @FXML
    private RadioButton RadioNonSmoking;


    /*---------------ComboBoxes----------------*/

    @FXML
    private ComboBox<VehicleClass> ClassComboBox;
    @FXML
    private ComboBox<VehicleCategory> CategoryComboBox;

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

    /*---------------Text fields ----------------*/

    @FXML
    private TextField clientNameTextField;
    @FXML
    private TextField clientPhoneTextField;

    /*---------------Text Areas ----------------*/

    @FXML
    private TextArea VehicleTextArea;

    /*--------------------Labels----------------*/
    @FXML
    private Label NameText;


    private AnchorPane[] Panes;

    public void initialize(){
        NameText.setText(Data.operatorUser);

        Panes = new AnchorPane[] { RegisterCarPane, RegisterClientPane, RentCarPane,
                ReturnCarPane, CalculatePricePane, CheckAvailableCarsPane, CheckRentingHistoryPane,
                CheckOperatorHistoryPane, CheckClientRatingsPane, CheckStatsOfCarsPane, MainMenuPane };

        HomeButton.setOnAction(e -> switchPane(MainMenuPane));
        RegisterCarButton.setOnAction(e -> switchPane(RegisterCarPane));
        RegisterClientButton.setOnAction(e -> switchPane(RegisterClientPane));
        RentCarButton.setOnAction(e -> switchPane(RentCarPane));
        ReturnCarButton.setOnAction(e -> switchPane(ReturnCarPane));
        CalculatePriceButton.setOnAction(e -> switchPane(CalculatePricePane));
        CheckAvailableCarsButton.setOnAction(e -> switchPane(CheckAvailableCarsPane));
        CheckRentingHistoryButton.setOnAction(e -> switchPane(CheckRentingHistoryPane));
        CheckOperatorHistoryButton.setOnAction(e -> switchPane(CheckOperatorHistoryPane));
        CheckClientRatingsButton.setOnAction(e -> switchPane(CheckClientRatingsPane));
        CheckStatsOfCarsButton.setOnAction(e -> switchPane(CheckStatsOfCarsPane));
    }


    /*--------------------------------------Functions for buttons-------------------------------------------*/


    public void exitSceneOnAction(){
        Stage stage = (Stage) OperatorCloseButton.getScene().getWindow();
        stage.close();
    }

    public void addClientOnAction(){
        if(clientPhoneTextField.getText().equals("")||clientNameTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the empty fields!");
            alert.show();
        }else{
            JavaPostgreSQL.addClient(clientNameTextField.getText(),clientPhoneTextField.getText());
        }
    }

    public void addVehicleOnAction(){
        if(ClassComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Class!");
            alert.show();
            return;
        }
        if(CategoryComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Category!");
            alert.show();
            return;
        }
        if(VehicleTextArea.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill out the vehicle characteristics!");
            alert.show();
            return;
        }
        if(!RadioSmoking.isSelected() && !RadioNonSmoking.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select if the vehicle is for smokers or not!");
            alert.show();
            return;
        }
        boolean isForSmokers = RadioSmoking.isSelected();

        JavaPostgreSQL.addVehicle(ClassComboBox.getValue().getId(),
                CategoryComboBox.getValue().getId(),
                Data.operatorId,
                VehicleTextArea.getText(),
                isForSmokers);
    }

    /*-------------------------------Functions for switching between panes-----------------------------------*/

    public void disableAllPanes(){
        for(AnchorPane pane : Panes){
            pane.setDisable(true);
            pane.setVisible(false);
        }
    }

    public void activatePane(AnchorPane pane){
        pane.setVisible(true);
        pane.setDisable(false);

        if(pane.equals(RegisterCarPane)){
            populateClassComboBox();
            populateCategoryComboBox();
        }
    }

    public void switchPane(AnchorPane pane){
        disableAllPanes();
        activatePane(pane);
    }

    /*-------------------------------Functions for populating ComboBoxes-----------------------------------*/

    private void populateClassComboBox(){
        VehicleClass[] strings = JavaPostgreSQL.getClassNames();
        ClassComboBox.setItems(FXCollections.observableArrayList(strings));
    }

    private void populateCategoryComboBox(){
        VehicleCategory[] strings = JavaPostgreSQL.getCategoryNames();
        CategoryComboBox.setItems(FXCollections.observableArrayList(strings));
    }
}
