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
import org.group29.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AdminRegisterCarController {

    /*---------------Radio Buttons----------------*/
    @FXML
    public ToggleGroup smoking;

    /*---------------ComboBoxes----------------*/
    @FXML
    private ComboBox<VehicleClass> ClassComboBox;
    @FXML
    private ComboBox<VehicleCategory> CategoryComboBox;
    @FXML
    private ComboBox<Firm> VehicleFirmComboBox;

    /*---------------Text Areas ----------------*/
    @FXML
    private TextArea VehicleTextArea;
    @FXML
    private FlowPane ImagePane;
    @FXML
    private Button deleteImgButton;
    @FXML
    private Button viewImgButton;

    private final ObjectProperty<StackPane> selectedImg = new SimpleObjectProperty<>();

    private final HashMap<Integer, VehiclePhoto> photos = new HashMap<>();
    private final HashMap<StackPane, Integer> thumbnails = new HashMap<>();
    private int idCounter = 0;

    @FXML
    protected void initialize() {
        populateClassComboBox();
        populateCategoryComboBox();
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
            photos.remove(thumbnails.get(selectedImg.getValue()));
            thumbnails.remove(selectedImg.getValue());

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
        stage.initOwner(VehicleTextArea.getScene().getWindow());

        stage.show();
    }

    @FXML
    public void addVehicleOnAction() {
        if(ClassComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Class!");
            alert.show();
            return;
        }
        if(CategoryComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Category!");
            alert.show();
            return;
        }
        if(VehicleFirmComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a firm!");
            alert.show();
            return;
        }
        if(VehicleTextArea.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill out the vehicle characteristics!");
            alert.show();
            return;
        }
        if(smoking.getSelectedToggle() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select if the vehicle is for smokers or not!");
            alert.show();
            return;
        }
        boolean isForSmokers = Boolean.parseBoolean((String)smoking.getSelectedToggle().getUserData());

        Vehicle newVehicle = new Vehicle(-1,
                ClassComboBox.getValue().getId(),
                CategoryComboBox.getValue().getId(),
                VehicleFirmComboBox.getValue().getId(),
                VehicleTextArea.getText(),
                isForSmokers);

        for(VehiclePhoto photo : photos.values()){
            newVehicle.addPhoto(photo);
        }

        newVehicle.commit();

        if(newVehicle.getId() != -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("New vehicle added!");
            alert.show();
        }
    }

    /*-------------------------------Functions for populating ComboBoxes-----------------------------------*/

    private void populateClassComboBox(){
        VehicleClass[] vehicles = JavaPostgreSQL.getClassNames();
        ClassComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }

    private void populateCategoryComboBox(){
        VehicleCategory[] categories = JavaPostgreSQL.getCategoryNames();
        CategoryComboBox.setItems(FXCollections.observableArrayList(categories));
    }

    private void populateFirmComboBox(){
        Firm[] firms = JavaPostgreSQL.getFirms();
        VehicleFirmComboBox.setItems(FXCollections.observableArrayList(firms));
    }

}
