package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.group29.entities.Return;

import java.util.Arrays;
import java.util.Collections;

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
        if(returnComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a return id to calculate!");
            alert.show();
            return;
        }

        Return selectedReturn = returnComboBox.getValue();
        double price = JavaPostgreSQL.getPrice(selectedReturn);
        if(price == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error has occurred");
            alert.show();
        }
        else
            priceTextField.setText(price + " лв.");
    }

    private void populateReturnComboBox(){
        Return[] returns = JavaPostgreSQL.getReturns();
        for(Return r : returns) {
            r.getRental().pull();
            r.getRental().getClient().pull();
        }
        Collections.reverse(Arrays.asList(returns));
        returnComboBox.setItems(FXCollections.observableArrayList(returns));
    }
}
