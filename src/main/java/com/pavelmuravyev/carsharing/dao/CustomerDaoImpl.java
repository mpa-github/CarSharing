package com.pavelmuravyev.carsharing.dao;

import com.pavelmuravyev.carsharing.db.H2DataBase;
import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Company;
import com.pavelmuravyev.carsharing.entity.Customer;
import com.pavelmuravyev.carsharing.entity.RentInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private final H2DataBase db;

    public CustomerDaoImpl(H2DataBase db) {
        this.db = db;
    }

    @Override
    public void createTableIfNotExists() throws SQLException, ClassNotFoundException {
        String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER" +
                     "(" +
                     "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                     "NAME VARCHAR NOT NULL UNIQUE," +
                     "RENTED_CAR_ID INTEGER, " +
                     "CONSTRAINT FK_CAR FOREIGN KEY (RENTED_CAR_ID) " +
                     "REFERENCES CAR(ID)" +
                     ")";
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public void insertCustomer(String name) throws SQLException, ClassNotFoundException {
        String sql = String.format("INSERT INTO CUSTOMER (NAME) " +
                                   "VALUES ('%s')", name);
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public List<Customer> selectAllCustomers() throws SQLException, ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT ID, NAME, RENTED_CAR_ID " +
                     "FROM CUSTOMER";
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int carID = resultSet.getInt("RENTED_CAR_ID");
                customers.add(new Customer(id, name, carID));
            }
            return customers;
        }
    }

    @Override
    public void returnRentedCar(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = String.format("UPDATE CUSTOMER " +
                                   "SET RENTED_CAR_ID = NULL " +
                                   "WHERE ID = %d", customer.getId());
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public void rentCar(Customer customer, Car car) throws SQLException, ClassNotFoundException {
        String sql = String.format("UPDATE CUSTOMER " +
                                   "SET RENTED_CAR_ID = %d " +
                                   "WHERE ID = %d", car.getId(), customer.getId());
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public RentInfo selectRentInfo(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = String.format("SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID, COMPANY.ID, COMPANY.NAME " +
                                   "FROM CUSTOMER " +
                                   "LEFT JOIN CAR " +
                                   "ON CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                                   "LEFT JOIN COMPANY " +
                                   "ON CAR.COMPANY_ID = COMPANY.ID " +
                                   "WHERE CUSTOMER.ID = %d", customer.getId());
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.first();
            int carId = resultSet.getInt("CAR.ID");
            String carName = resultSet.getString("CAR.NAME");
            int companyID = resultSet.getInt("CAR.COMPANY_ID");
            Car car = new Car(carId, carName, companyID);
            int companyId = resultSet.getInt("COMPANY.ID");
            String companyName = resultSet.getString("COMPANY.NAME");
            Company company = new Company(companyId, companyName);
            return new RentInfo(car, company);
        }
    }
}
