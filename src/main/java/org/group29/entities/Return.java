package org.group29.entities;

import java.sql.Date;

public class Return {
    int id,rental_id,condition_id,id_price;
    Date return_date;

    public Return(int id, int rental_id, int condition_id, int id_price, Date return_date) {
        this.id = id;
        this.rental_id = rental_id;
        this.condition_id = condition_id;
        this.id_price = id_price;
        this.return_date = return_date;
    }

    public int getId() {
        return id;
    }

    public int getRental_id() {
        return rental_id;
    }

    public int getCondition_id() {
        return condition_id;
    }

    public int getId_price() {
        return id_price;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRental_id(int rental_id) {
        this.rental_id = rental_id;
    }

    public void setCondition_id(int condition_id) {
        this.condition_id = condition_id;
    }

    public void setId_price(int id_price) {
        this.id_price = id_price;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    @Override
    public String toString() {
        return "" + id ;
    }
}
