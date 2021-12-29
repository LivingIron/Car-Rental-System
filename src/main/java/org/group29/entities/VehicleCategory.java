package org.group29.entities;

public class VehicleCategory {
    private final int id;
    private final String name;

    public VehicleCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return this.name;
    }
}
