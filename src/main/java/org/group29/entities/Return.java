package org.group29.entities;

import org.group29.JavaPostgreSQL;

import java.sql.Date;

public class Return {
    int id;
    Rental rental;
    Date return_date;

    public Return(int id, Rental rental, Date return_date) {
        this.id = id;
        this.rental = rental;
        this.return_date = return_date;
    }

    public void commit(){
        if(id == -1)
            id = JavaPostgreSQL.addReturn(this);
    }

    public void pull(){
        if(id != -1)
            JavaPostgreSQL.getReturn(this);
    }

    public int getId() {
        return id;
    }

    public Rental getRental() {
        return rental;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    @Override
    public String toString() {
        return "" + id ;
    }
}
