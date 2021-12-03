package org.group29.entities;

import org.group29.JavaPostgreSQL;

public class Firm {
    private int id;
    private String name;

    public Firm(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void commit(){
        if(id == -1)
            id = JavaPostgreSQL.addFirm(this);
        else
            JavaPostgreSQL.updateFirm(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.name;
    }
}
