package org.group29.entities;

public class VehicleClass {
    private final int id;
    private final String name;

    public VehicleClass(int id, String name) {
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
