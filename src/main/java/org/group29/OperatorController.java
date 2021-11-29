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
    private BorderPane mainPane;

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
