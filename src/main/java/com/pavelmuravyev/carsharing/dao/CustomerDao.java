package com.pavelmuravyev.carsharing.dao;

import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Customer;
import com.pavelmuravyev.carsharing.entity.RentInfo;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {

    void createTableIfNotExists() throws SQLException, ClassNotFoundException;
    void insertCustomer(String name) throws SQLException, ClassNotFoundException;
    List<Customer> selectAllCustomers() throws SQLException, ClassNotFoundException;
    void returnRentedCar(Customer customer) throws SQLException, ClassNotFoundException;
    void rentCar(Customer customer, Car car) throws SQLException, ClassNotFoundException;
    RentInfo selectRentInfo(Customer customer) throws SQLException, ClassNotFoundException;
}
