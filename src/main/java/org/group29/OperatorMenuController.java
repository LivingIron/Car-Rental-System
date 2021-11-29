package org.group29;

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
    private Button CheckOperatorHistoryButton;
    @FXML
    private Button CheckClientRatingsButton;
    @FXML
    private Button CheckStatsOfCarsButton;

    public void initialize(){
        RegisterCarButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"RegisterCar"));
        RegisterClientButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"RegisterClient"));
        RentCarButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"RentCar"));
        ReturnCarButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"ReturnCar"));
        CalculatePriceButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"CalculatePrice"));
        CheckAvailableCarsButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"CheckAvailableCars"));
        CheckRentingHistoryButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"CheckRentingHistory"));
        CheckOperatorHistoryButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"CheckOperatorHistory"));
        CheckClientRatingsButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"CheckClientRatings"));
        CheckStatsOfCarsButton.setOnAction(e -> FxmlLoader.switchPane(Data.operatorMainPane,"CheckStatsOfCars"));
    }
}
