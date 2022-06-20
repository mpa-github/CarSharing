package com.pavelmuravyev.carsharing.dao;

import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Company;
import com.pavelmuravyev.carsharing.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CarDao {

    void createTableIfNotExists() throws SQLException, ClassNotFoundException;
    void insertCar(String name, Company company) throws SQLException, ClassNotFoundException;
    Car selectCarByCustomer(Customer customer) throws SQLException, ClassNotFoundException;
    List<Car> selectAllCars() throws SQLException, ClassNotFoundException;
    List<Car> selectCarsByCompany(Company company) throws SQLException, ClassNotFoundException;
    List<Car> selectNotRentedCarsByCompany(Company company) throws SQLException, ClassNotFoundException;
}
