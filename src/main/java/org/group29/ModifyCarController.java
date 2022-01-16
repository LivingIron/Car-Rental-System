package org.group29;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.group29.entities.Condition;
import org.group29.entities.Firm;
import org.group29.entities.Vehicle;
import org.group29.entities.VehiclePhoto;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ModifyCarController {
    @FXML
    private ComboBox<Vehicle> vehicleComboBox;
    @FXML
    private TextArea characteristicsField;
    @FXML
    private TextArea damagesField;
    @FXML
    private ComboBox<Firm> firmComboBox;
    @FXML
    private FlowPane ImagePane;
    @FXML
    private Button deleteImgButton;
    @FXML
    private Button viewImgButton;

    private final ObjectProperty<StackPane> selectedImg = new SimpleObjectProperty<>();
    private final HashMap<Integer, VehiclePhoto> photos = new HashMap<>();
    private final HashMap<StackPane, Integer> thumbnails = new HashMap<>();
    private final List<Integer> photosToRemove = new ArrayList<>();
    private int idCounter = 10000;

    private Vehicle selectedVehicle;
    private Condition selectedCondition;

    @FXML
    private void initialize(){
        populateVehicleComboBox();
        populateFirmComboBox();
        selectedImg.addListener((obs, oldImg, newImg) -> {
            if (oldImg != null) {
                oldImg.setStyle("");
            }
            if (newImg != null) {
                viewImgButton.setDisable(false);
                newImg.setStyle("-fx-border-style: solid; -fx-border-color: grey; -fx-border-width: 5;");
            }
        });
    }

    @FXML
    public void onVehicleSelection(){
        characteristicsField.setDisable(false);
        damagesField.setDisable(false);
        firmComboBox.setDisable(false);

        selectedVehicle = vehicleComboBox.getValue();
        selectedCondition = Condition.getConditionByCar(selectedVehicle);
        Optional<Firm> foundSelected = firmComboBox.getItems().stream().filter(f -> f.getId() == selectedVehicle.getFirm_id()).findFirst();

        foundSelected.ifPresent(firm -> firmComboBox.setValue(firm));
        characteristicsField.setText(selectedVehicle.getCharacteristics());
        damagesField.setText(selectedCondition.getDamages());

        selectedVehicle.setPhotos(Arrays.asList(JavaPostgreSQL.getPhotosByVehicle(selectedVehicle)));
        thumbnails.clear();
        photos.clear();
        photosToRemove.clear();
        ImagePane.getChildren().clear();
        selectedImg.set(null);
        for(VehiclePhoto photo : selectedVehicle.getPhotos()){
            ImageView imgView = new ImageView(photo.toImage(0, 80, true));
            StackPane.setAlignment(imgView, Pos.CENTER);

            StackPane imgContainer = new StackPane();
            imgContainer.getChildren().add(imgView);
            imgView.setOnMouseClicked(e -> selectedImg.set(imgContainer));

            ImagePane.getChildren().add(imgContainer);
            thumbnails.put(imgContainer, photo.getId());
            photos.put(photo.getId(), photo);
        }
        if(thumbnails.size() > 0)
            deleteImgButton.setDisable(false);
    }

    @FXML
    public void addImageOnAction() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(ImagePane.getScene().getWindow());
        if (selectedFile != null) {
            VehiclePhoto photo = new VehiclePhoto(-1);
            photo.setByteArray(selectedFile);
            photos.put(++idCounter, photo);

            ImageView imgView = new ImageView(photo.toImage(0, 80, true));
            StackPane.setAlignment(imgView, Pos.CENTER);

            StackPane imgContainer = new StackPane();
            imgContainer.getChildren().add(imgView);
            imgView.setOnMouseClicked(e -> selectedImg.set(imgContainer));

            thumbnails.put(imgContainer, idCounter);
            ImagePane.getChildren().add(imgContainer);
            deleteImgButton.setDisable(false);
        }
    }

    @FXML
    public void deleteImageOnAction(){
        if(selectedImg.getValue() != null){
            int id = thumbnails.get(selectedImg.getValue());
            thumbnails.remove(selectedImg.getValue());
            if(photos.get(id).getId() != -1) photosToRemove.add(id);
            photos.remove(id);

            ImagePane.getChildren().remove(selectedImg.getValue());
            selectedImg.set(null);
            viewImgButton.setDisable(true);

            if(ImagePane.getChildren().size() == 0) deleteImgButton.setDisable(true);
        }
    }

    @FXML
    public void viewImageOnAction(){
        Stage stage = new Stage();
        StackPane myPane = new StackPane();
        StackPane selected = selectedImg.getValue();
        Image image = photos.get(thumbnails.get(selected)).toImage(0, 720, true);
        myPane.getChildren().add(new ImageView(image));
        Scene scene = new Scene(myPane);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(damagesField.getScene().getWindow());

        stage.show();
    }

    @FXML
    public void onUpdateAction(){
        if(vehicleComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select operator to edit!");
            alert.show();
            return;
        }
        if(characteristicsField.getLength() == 0
                && damagesField.getLength() == 0
                && firmComboBox.getValue().getId() == selectedVehicle.getFirm_id()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please change something!");
            alert.show();
            return;
        }

        boolean vFlag = false;
        boolean cFlag = false;
        if(characteristicsField.getLength() > 0 && !characteristicsField.getText().equals(selectedVehicle.getCharacteristics())){
            selectedVehicle.setCharacteristics(characteristicsField.getText());
            vFlag = true;
        }
        if(!damagesField.getText().equals(selectedCondition.getDamages())){
            selectedCondition.setDamages(damagesField.getText());
            cFlag = true;
        }
        if(firmComboBox.getValue().getId() != selectedVehicle.getFirm_id()){
            selectedVehicle.setFirm_id(firmComboBox.getValue().getId());
            vFlag = true;
        }
        if(!photosToRemove.isEmpty()){
            for(int id : photosToRemove){
                selectedVehicle.removePhoto(id);
            }
            vFlag = true;
        }
        if(!photos.isEmpty()){
            for(VehiclePhoto photo : photos.values()){
                if(photo.getId() == -1) {
                    selectedVehicle.addPhoto(photo);
                }
            }
            vFlag = true;
        }
        if(vFlag) selectedVehicle.commit();
        if(cFlag) selectedCondition.commit();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vehicle has been modified");
        alert.show();
    }

    private void populateVehicleComboBox(){
        Vehicle[] vehicles = JavaPostgreSQL.getVehicles();
        vehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }
    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        firmComboBox.setItems(FXCollections.observableArrayList(firms));
    }
}
