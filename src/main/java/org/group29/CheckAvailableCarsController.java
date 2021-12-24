package org.group29;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.group29.entities.Vehicle;
import org.group29.entities.VehiclePhoto;

import java.sql.Date;
import java.time.LocalDate;

public class CheckAvailableCarsController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ListView<Vehicle> vehicleList;

    @FXML
    private void checkButtonAction(){
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a date!");
            alert.show();
            return;
        }
        if(startDatePicker.getValue().isBefore(LocalDate.now()) || endDatePicker.getValue().isBefore(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an actual date!");
            alert.show();
            return;
        }
        if(endDatePicker.getValue().isBefore(startDatePicker.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("End date is before start date!");
            alert.show();
            return;
        }

        Date startDate = Date.valueOf(startDatePicker.getValue());
        Date endDate = Date.valueOf(endDatePicker.getValue());

        Vehicle[] cars = JavaPostgreSQL.getAvailableVehicles(startDate, endDate);

        vehicleList.setItems(FXCollections.observableArrayList(cars));
        vehicleList.setCellFactory(lv -> {
            ListCell<Vehicle> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem viewItem = new MenuItem();
            viewItem.textProperty().bind(Bindings.format("View photos"));
            viewItem.setOnAction(event -> {
                Vehicle item = cell.getItem();
                VehiclePhoto[] photos = JavaPostgreSQL.getPhotosByVehicle(item);
                if(photos.length == 0){
                    Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                    newAlert.setContentText("Selected vehicle has no photos");
                    newAlert.show();
                }
                else createGalleryWindow(photos);
            });
            contextMenu.getItems().add(viewItem);

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.textProperty().bind(cell.itemProperty().asString());
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }

    private void createGalleryWindow(VehiclePhoto[] photos){
        Stage stage = new Stage();
        FlowPane flowPane = new FlowPane();
        for(VehiclePhoto photo : photos) {
            flowPane.getChildren().add(new ImageView(photo.toImage(0, 420, true)));
        }
        ScrollPane scroll = new ScrollPane(flowPane);
        scroll.setPrefWidth(1280);
        scroll.setPrefHeight(720);
        scroll.fitToWidthProperty().set(true);
        Scene scene = new Scene(scroll);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Photo gallery");
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(vehicleList.getScene().getWindow());

        stage.show();
    }
}
