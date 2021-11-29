package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.group29.entities.Rental;
import org.group29.entities.Vehicles;

public class ReturnCarController {
    @FXML
    private ComboBox<Rental> returnCarComboBox;

    @FXML
    protected void initialize(){
        populateRentalComboBox();
    }


    private void populateRentalComboBox(){
        Rental[] strings = JavaPostgreSQL.getRentals();
        returnCarComboBox.setItems(FXCollections.observableArrayList(strings));
    }
}
