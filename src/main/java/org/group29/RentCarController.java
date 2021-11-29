package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.group29.entities.Client;
import org.group29.entities.VehicleClass;
import org.group29.entities.Vehicles;

public class RentCarController {

    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private ComboBox<Vehicles> carComboBox;

    @FXML
    protected void initialize(){
        populateVehiclesComboBox();
        populateClientComboBox();
    }

    private void populateVehiclesComboBox(){
        Vehicles[] strings = JavaPostgreSQL.getVehicles();
        carComboBox.setItems(FXCollections.observableArrayList(strings));
    }
    private void populateClientComboBox(){
        Client[] strings = JavaPostgreSQL.getClients();
        clientComboBox.setItems(FXCollections.observableArrayList(strings));
    }

}
