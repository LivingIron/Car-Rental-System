package org.group29;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminMenuController {

    @FXML
    private Button RegisterCarButton;
    @FXML
    private Button RegisterOperatorButton;
    @FXML
    private Button CheckOperatorHistoryButton;
    @FXML
    private Button RegisterFirmButton;
    @FXML
    private Button ModifyFirmButton;
    @FXML
    private Button ModifyOperatorButton;
    @FXML
    private Button ModifyCarButton;

    private final StringProperty selectedMenu = new SimpleStringProperty();

    public StringProperty valueProperty() {
        return selectedMenu;
    }

    public void initialize(){
        RegisterFirmButton.setOnAction(e -> selectedMenu.set("RegisterFirm"));
        RegisterCarButton.setOnAction(e -> selectedMenu.set("AdminRegisterCar"));
        RegisterOperatorButton.setOnAction(e -> selectedMenu.set("RegisterOperator"));
        CheckOperatorHistoryButton.setOnAction(e -> selectedMenu.set("CheckOperatorHistory"));
        ModifyFirmButton.setOnAction(e -> selectedMenu.set("ModifyFirm"));
        ModifyOperatorButton.setOnAction(e -> selectedMenu.set("ModifyOperator"));
        ModifyCarButton.setOnAction(e -> selectedMenu.set("ModifyCar"));
    }
}
