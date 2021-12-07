package org.group29.entities;

import org.group29.JavaPostgreSQL;

public class Vehicle {

    private int id, classification, category, firm_id;
    private String characteristics;
    private boolean smoking;

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
        }
        else
            JavaPostgreSQL.updateVehicle(this);
    }

    public void pull(){
        if(id != -1)
            JavaPostgreSQL.getVehicle(this);
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
