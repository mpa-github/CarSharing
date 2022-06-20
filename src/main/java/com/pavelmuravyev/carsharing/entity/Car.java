package com.pavelmuravyev.carsharing.entity;

import java.util.Objects;

public class Car {

    private int id;
    private String name;
    private int companyID;

    public Car(int id, String name, int companyID) {
        this.id = id;
        this.name = name;
        this.companyID = companyID;
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

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    @Override
    public String toString() {
        return "Car{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", companyID=" + companyID + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && companyID == car.companyID && Objects.equals(name, car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, companyID);
    }
}
