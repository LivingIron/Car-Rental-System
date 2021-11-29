package org.group29.entities;

public class Client {

    private int id,firm_id,rating;
    private String name,phone;

    public Client(int id, int firm_id, int rating, String name, String phone) {
        this.id = id;
        this.firm_id = firm_id;
        this.rating = rating;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name;
    }
}
