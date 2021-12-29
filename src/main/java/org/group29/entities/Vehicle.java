package org.group29.entities;

import org.group29.JavaPostgreSQL;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private int id, classification, category, firm_id;
    private String characteristics;
    private boolean smoking;
    private List<VehiclePhoto> photos = new ArrayList<>();

    public Vehicle(int id, int classification, int category, int firm_id, String characteristics, boolean smoking) {
        this.id = id;
        this.classification = classification;
        this.category = category;
        this.firm_id = firm_id;
        this.characteristics = characteristics;
        this.smoking = smoking;
    }

    public Vehicle(int id){
        this.id = id;
    }

    public void commit(){
        if(id == -1){
            id = JavaPostgreSQL.addVehicle(this);
            for(VehiclePhoto photo : photos){
                photo.setCar_id(id);
                photo.commit();
            }
        }
        else{
            JavaPostgreSQL.updateVehicle(this);
            for(VehiclePhoto photo : photos){
                if(photo.getId() == -1){
                    photo.setCar_id(id);
                    photo.commit();
                }
            }
        }
    }

    public void pull(){
        if(id != -1)
            JavaPostgreSQL.getVehicle(this);
    }

    public void addPhoto(VehiclePhoto photo){
        photos.add(photo);
    }

    public void removePhoto(int id){
        VehiclePhoto foundPhoto = photos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if(foundPhoto != null){
            foundPhoto.delete();
            photos.remove(foundPhoto);
        }
    }

    public void setPhotos(List<VehiclePhoto> photos){
        this.photos = new ArrayList<>(photos);
    }

    public List<VehiclePhoto> getPhotos(){
        return photos;
    }

    public int getId() {
        return id;
    }

    public int getClassification() {
        return classification;
    }

    public int getCategory() {
        return category;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s", id, characteristics, (smoking ? "Smoking" : "Non-smoking"));
    }
}
