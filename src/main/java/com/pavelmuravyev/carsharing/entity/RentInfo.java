package com.pavelmuravyev.carsharing.entity;

import java.util.Objects;

public class RentInfo {

    private Car car;
    private Company company;

    public RentInfo(Car car, Company company) {
        this.car = car;
        this.company = company;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "RentInfo{" +
               "car=" + car +
               ", company=" + company + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentInfo rentInfo = (RentInfo) o;
        return Objects.equals(car, rentInfo.car) && Objects.equals(company, rentInfo.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, company);
    }
}
