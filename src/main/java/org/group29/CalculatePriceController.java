package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

    private void populateReturnComboBox(){
        Return[] strings = JavaPostgreSQL.getReturns();
        returnComboBox.setItems(FXCollections.observableArrayList(strings));
    }


}
