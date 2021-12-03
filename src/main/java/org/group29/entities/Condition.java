package org.group29.entities;

import org.group29.JavaPostgreSQL;

public class Condition {
    private int id, odometer, vehicle_id;
    private String damages;

    public Condition(int id, int odometer, int vehicle_id, String damages) {
        this.id = id;
        this.odometer = odometer;
        this.vehicle_id = vehicle_id;
        this.damages = damages;
    }

    public Condition(int id){
        this.id = id;
    }

    public void commit(){
        if(id != -1)
            JavaPostgreSQL.updateCondition(this);
    }

    public void pull(){
        if(id != -1)
            JavaPostgreSQL.getCondition(this);
    }

    public static Condition getConditionByCar(Vehicle vehicle){
        return JavaPostgreSQL.getCarCondition(vehicle.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getDamages() {
        return damages;
    }

    public void addDamages(String newDamages){
        newDamages = newDamages.strip();
        if(newDamages.length() == 0) return;

        if(damages.endsWith("\n"))
            damages += newDamages;
        else
            damages += "\n" + newDamages;
    }

    public void setDamages(String damages) {
        this.damages = damages;
    }
}
