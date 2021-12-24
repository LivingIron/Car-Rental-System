package org.group29;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.group29.entities.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CheckStatsOfCarsController {
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, String> classColumn;
    @FXML
    private TableColumn<Vehicle, String> categoryColumn;
    @FXML
    private TableColumn<Vehicle, String> characteristicsColumn;
    @FXML
    private TableColumn<Vehicle, String> smokingColumn;
    @FXML
    private TableColumn<Vehicle, String> odometerColumn;
    @FXML
    private TableColumn<Vehicle, String> damagesColumn;
    @FXML
    private MenuButton classMenuButton;
    @FXML
    private MenuButton categoryMenuButton;
    @FXML
    private MenuButton smokingMenuButton;

    private final HashMap<Integer, String> classes = new HashMap<>();
    private final HashMap<Integer, String> categories = new HashMap<>();
    private Vehicle[] vehicles;
    private final HashMap<Vehicle, Condition> conditions = new HashMap<>();

    @FXML
    private void initialize(){
        populateClassComboBox();
        populateCategoryComboBox();
        vehicles = Arrays.stream(JavaPostgreSQL.getVehicles()).filter(v -> v.getFirm_id() == Data.operator.getFirm_id()).toArray(Vehicle[]::new);
        for(Vehicle v : vehicles){
            conditions.put(v, Condition.getConditionByCar(v));
        }
    }

    public void searchOnAction(){
        List<String> selectedClasses = classMenuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected())
                .map(MenuItem::getText)
                .collect(Collectors.toList());
        List<String> selectedCategories = categoryMenuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected())
                .map(MenuItem::getText)
                .collect(Collectors.toList());
        List<String> selectedSmoking = smokingMenuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected())
                .map(MenuItem::getText)
                .collect(Collectors.toList());

        List<Vehicle> filteredVehicles = Arrays.stream(vehicles)
                .filter(c -> selectedClasses.contains(classes.get(c.getClassification()))
                        && selectedCategories.contains(categories.get(c.getCategory()))
                        && selectedSmoking.contains(String.valueOf(c.isSmoking())))
                .collect(Collectors.toList());

        classColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(classes.get(p.getValue().getClassification())));
        categoryColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(categories.get(p.getValue().getCategory())));
        characteristicsColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getCharacteristics()));
        smokingColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(Boolean.toString(p.getValue().isSmoking())));
        odometerColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(Integer.toString(conditions.get(p.getValue()).getOdometer())));
        damagesColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(conditions.get(p.getValue()).getDamages()));

        if(filteredVehicles.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No vehicles found");
            alert.show();
            return;
        }

        vehicleTable.setItems(FXCollections.observableList(filteredVehicles));
        vehicleTable.setRowFactory(tv -> {
            TableRow<Vehicle> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem viewItem = new MenuItem();
            viewItem.textProperty().bind(Bindings.format("View photos"));
            viewItem.setOnAction(event -> {
                Vehicle item = row.getItem();
                VehiclePhoto[] photos = JavaPostgreSQL.getPhotosByVehicle(item);
                if(photos.length == 0){
                    Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                    newAlert.setContentText("Selected vehicle has no photos");
                    newAlert.show();
                }
                else createGalleryWindow(photos);
            });
            contextMenu.getItems().add(viewItem);

            row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(contextMenu);
                }
            });
            return row;
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
        stage.initOwner(vehicleTable.getScene().getWindow());

        stage.show();
    }

    private void populateClassComboBox(){
        VehicleClass[] strings = JavaPostgreSQL.getClassNames();
        ObservableList<VehicleClass> classes = FXCollections.observableArrayList(strings);

        for(VehicleClass c : classes){
            this.classes.put(c.getId(), c.getName());

            CheckMenuItem newItem = new CheckMenuItem(c.toString());
            newItem.setSelected(true);
            classMenuButton.getItems().add(newItem);
        }
    }

    private void populateCategoryComboBox(){
        VehicleCategory[] strings = JavaPostgreSQL.getCategoryNames();
        ObservableList<VehicleCategory> categories = FXCollections.observableArrayList(strings);

        for(VehicleCategory c : categories){
            this.categories.put(c.getId(), c.getName());

            CheckMenuItem newItem = new CheckMenuItem(c.toString());
            newItem.setSelected(true);
            categoryMenuButton.getItems().add(newItem);
        }
    }
}
