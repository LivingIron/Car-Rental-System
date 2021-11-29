package org.group29;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group29.entities.Rental;
import org.group29.entities.Vehicles;

import java.sql.Date;
import java.time.LocalDate;

public class ReturnCarController {
    @FXML
    private ComboBox<Rental> returnCarComboBox;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private TextArea returnTextArea;
    @FXML
    private TextField odometerTextField;

    @FXML
    protected void initialize(){
        populateRentalComboBox();
    }

    public void enableFullForm(){

        if(returnCarComboBox.getValue()!=null){
            returnDatePicker.setDisable(false);
            returnTextArea.setDisable(false);
            odometerTextField.setDisable(false);

            returnTextArea.setText(JavaPostgreSQL.getCarCondition(JavaPostgreSQL.rentalIdToCarId(returnCarComboBox.getValue().getId())));
            odometerTextField.setText( JavaPostgreSQL.getCarOdometer(JavaPostgreSQL.rentalIdToCarId(returnCarComboBox.getValue().getId())));
        }
    }

    public void rentCarOnAction(){
        if(returnCarComboBox.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a rental id !");
            alert.show();
            return;
        }
        if(returnDatePicker.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date!");
            alert.show();
            return;
        }
        if(returnDatePicker.getValue().isBefore(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an actual date!");
            alert.show();
            return;
        }

        JavaPostgreSQL.returnCar(   returnCarComboBox.getValue().getId(),
                                    Date.valueOf(returnDatePicker.getValue()),
                                    JavaPostgreSQL.carIdToConditionId(JavaPostgreSQL.rentalIdToCarId(Integer.parseInt(returnCarComboBox.getValue().toString()))),
                                    returnTextArea.getText(),
                                    Integer.parseInt(odometerTextField.getText())
                                    );

    }


    private void populateRentalComboBox(){
        Rental[] strings = JavaPostgreSQL.getRentals();
        returnCarComboBox.setItems(FXCollections.observableArrayList(strings));
    }
}
