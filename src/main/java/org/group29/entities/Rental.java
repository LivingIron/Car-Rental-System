package org.group29.entities;

import java.sql.Date;

public class Rental {
    private int id,car_id,client_id,condition_id,duration;
    private Date rental_date;
    private boolean is_returned;

    public Rental(int id, int car_id, int client_id, int condition_id, int duration, Date rental_date, boolean is_returned) {
        this.id = id;
        this.car_id = car_id;
        this.client_id = client_id;
        this.condition_id = condition_id;
        this.duration = duration;
        this.rental_date = rental_date;
        this.is_returned = is_returned;
    }

    public int getId() {
        return id;
    }

    public int getCar_id() {
        return car_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getCondition_id() {
        return condition_id;
    }

    public int getDuration() {
        return duration;
    }

    public Date getRental_date() {
        return rental_date;
    }

    public boolean isIs_returned() {
        return is_returned;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public void setCondition_id(int condition_id) {
        this.condition_id = condition_id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRental_date(Date rental_date) {
        this.rental_date = rental_date;
    }

    public void setIs_returned(boolean is_returned) {
        this.is_returned = is_returned;
    }

    @Override
    public String toString() {
        return ""+id;
    }
}
