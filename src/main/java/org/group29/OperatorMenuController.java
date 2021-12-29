package org.group29;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OperatorMenuController {

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
    private Button CheckClientRatingsButton;
    @FXML
    private Button CheckStatsOfCarsButton;

    private final StringProperty selectedMenu = new SimpleStringProperty();

    public StringProperty valueProperty() {
        return selectedMenu;
    }

    public void initialize(){
        RegisterCarButton.setOnAction(e -> selectedMenu.set("RegisterCar"));
        RegisterClientButton.setOnAction(e -> selectedMenu.set("RegisterClient"));
        RentCarButton.setOnAction(e -> selectedMenu.set("RentCar"));
        ReturnCarButton.setOnAction(e -> selectedMenu.set("ReturnCar"));
        CalculatePriceButton.setOnAction(e -> selectedMenu.set("CalculatePrice"));
        CheckAvailableCarsButton.setOnAction(e -> selectedMenu.set("CheckAvailableCars"));
        CheckRentingHistoryButton.setOnAction(e -> selectedMenu.set("CheckRentingHistory"));
        CheckClientRatingsButton.setOnAction(e -> selectedMenu.set("CheckClientRatings"));
        CheckStatsOfCarsButton.setOnAction(e -> selectedMenu.set("CheckStatsOfCars"));
    }
}
