package org.group29.entities;

import org.group29.JavaPostgreSQL;

import java.sql.Date;

public class Rental {
    private int id, duration, firm_id;
    private Vehicle vehicle;
    private Client client;
    private Condition condition;
    private Date rental_date;
    private boolean is_returned;

    public Rental(int id, int firm_id, Vehicle vehicle, Client client, Condition condition, int duration, Date rental_date, boolean is_returned){
        this.id = id;
        this.firm_id = firm_id;
        this.vehicle = vehicle;
        this.client = client;
        this.condition = condition;
        this.duration = duration;
        this.rental_date = rental_date;
        this.is_returned = is_returned;
    }

    public Rental(int id) {
        this.id = id;
    }

    public void commit(){
        if(id == -1)
            id = JavaPostgreSQL.addRental(this);
        else
            JavaPostgreSQL.updateRental(this);
    }

    public void pull(){
        if(id != -1)
            JavaPostgreSQL.getRental(this);
    }

    public int getId() {
        return id;
    }

    public int getFirm_id(){
        return firm_id;
    }

    public int getDuration() {
        return duration;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Client getClient() {
        return client;
    }

    public Date getRental_date() {
        return rental_date;
    }

    public boolean isReturned() {
        return is_returned;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirm_id(int firm_id){
        this.firm_id = firm_id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setRental_date(Date rental_date) {
        this.rental_date = rental_date;
    }

    public void setReturned(boolean is_returned) {
        this.is_returned = is_returned;
    }

    @Override
    public String toString() {
        client.pull();
        return String.format("%d | %s | %s | %d days", id, client.getName(), rental_date.toString(), duration);
    }
}
