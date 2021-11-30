package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.group29.entities.Return;

public class CalculatePriceController {

    @FXML
    private ComboBox<Return> returnComboBox;
    @FXML
    private TextField priceTextField;

    @FXML
    protected void initialize(){
        populateReturnComboBox();
    }

    public void calculateOnAction(){
        if(returnComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a return id to calculate !");
            alert.show();
            return;
        }

        JavaPostgreSQL.calculatePrice(  priceTextField,
                                        JavaPostgreSQL.priceIdToDays(returnComboBox.getValue().getId_price()),
                                        JavaPostgreSQL.priceIdToKilometers(returnComboBox.getValue().getId_price()),
                                        JavaPostgreSQL.priceIdToDmgCount(returnComboBox.getValue().getId_price()),
                                        JavaPostgreSQL.priceIdToClassId(returnComboBox.getValue().getId_price()));
    }

    private void populateReturnComboBox(){
        Return[] strings = JavaPostgreSQL.getReturns();
        returnComboBox.setItems(FXCollections.observableArrayList(strings));
    }


}
