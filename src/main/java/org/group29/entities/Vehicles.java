package org.group29.entities;

public class Vehicles {

    private int id,vehicleClass,vehicleCategory,firm_id;
    private String characteristics;
    private boolean smoking,is_rented;

    public Vehicles(int id, int vehicleClass, int vehicleCategory, int firm_id, String characteristics, boolean smoking, boolean is_rented) {
        this.id = id;
        this.vehicleClass = vehicleClass;
        this.vehicleCategory = vehicleCategory;
        this.firm_id = firm_id;
        this.characteristics = characteristics;
        this.smoking = smoking;
        this.is_rented = is_rented;
    }

    public int getId() {
        return id;
    }

    public int getVehicleClass() {
        return vehicleClass;
    }

    public int getVehicleCategory() {
        return vehicleCategory;
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

    public boolean isIs_rented() {
        return is_rented;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVehicleClass(int vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public void setVehicleCategory(int vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
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

    public void setIs_rented(boolean is_rented) {
        this.is_rented = is_rented;
    }

    @Override
    public String toString() {
        return "Vehicle  " +
                "id :" + id ;
    }
}
