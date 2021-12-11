package org.group29;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group29.entities.Condition;
import org.group29.entities.Vehicle;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;

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
    private final Vehicle[] vehicles = JavaPostgreSQL.getVehicles();
    private final HashMap<Vehicle, Condition> conditions = new HashMap<>();

    @FXML
    private void initialize(){
        populateClassComboBox();
        populateCategoryComboBox();
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
