package org.group29.entities;

import javafx.scene.image.Image;
import org.group29.JavaPostgreSQL;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class VehiclePhoto {
    private int id, car_id;
    private byte[] byteArray;

    public VehiclePhoto(int id, int car_id, byte[] byteArray) {
        this.id = id;
        this.car_id = car_id;
        this.byteArray = byteArray;
    }
    public VehiclePhoto(int id){
        this.id = id;
    }

    public void commit(){
        if(id == -1) id = JavaPostgreSQL.addPhoto(this);
    }

    public void pull(){
        if(id != -1) JavaPostgreSQL.getPhoto(this);
    }

    public void delete(){
        if(id != -1) JavaPostgreSQL.deletePhoto(this);
    }

    public void setByteArray(File file) throws IOException {
        byteArray = Files.readAllBytes(file.toPath());
    }

    public Image toImage(){
        return new Image(new ByteArrayInputStream(byteArray));
    }

    public Image toImage(int width, int height, boolean preserveRatio){
        Image firstImage = this.toImage();
        if(firstImage.getHeight() <= height) return firstImage;
        else return new Image(new ByteArrayInputStream(byteArray), width, height, preserveRatio, true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
