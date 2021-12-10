package org.group29.entities;

import org.group29.JavaPostgreSQL;

public class Operator {
    int id, firm_id;
    String username, password;

    public Operator(int id, int firm_id, String username, String password) {
        this.id = id;
        this.firm_id = firm_id;
        this.username = username;
        this.password = password;
    }

    public Operator(int id){
        this.id = id;
    }

    public void commit(){
        if(id == -1)
            id = JavaPostgreSQL.addOperator(this);
        else
            JavaPostgreSQL.updateOperator(this);
    }

    public void pull(){
        if(id != -1)
            JavaPostgreSQL.getOperator(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return username;
    }
}
