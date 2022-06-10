package com.pavelmuravyev.carsharing.dao;

import com.pavelmuravyev.carsharing.db.H2DataBase;
import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Company;
import com.pavelmuravyev.carsharing.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    private final H2DataBase db;

    public CarDaoImpl(H2DataBase db) {
        this.db = db;
    }

    @Override
    public void createTableIfNotExists() throws SQLException, ClassNotFoundException {
        String sql = "CREATE TABLE IF NOT EXISTS CAR" +
                     "(" +
                     "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                     "NAME VARCHAR NOT NULL UNIQUE," +
                     "COMPANY_ID INTEGER NOT NULL, " +
                     "CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID) " +
                     "REFERENCES COMPANY(ID)" +
                     ")";
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public void insertCar(String name, Company company) throws SQLException, ClassNotFoundException {
        String sql = String.format("INSERT INTO CAR (NAME, COMPANY_ID) " +
                                   "VALUES ('%s', %d)", name, company.getId());
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public Car selectCarByCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = String.format("SELECT ID, NAME, COMPANY_ID " +
                                   "FROM CAR " +
                                   "WHERE ID = %d", customer.getCarID());
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            int companyID = resultSet.getInt("COMPANY_ID");
            return new Car(id, name, companyID);
        }
    }

    @Override
    public List<Car> selectAllCars() throws SQLException, ClassNotFoundException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT ID, NAME, COMPANY_ID " +
                     "FROM CAR";
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyID = resultSet.getInt("COMPANY_ID");
                cars.add(new Car(id, name, companyID));
            }
            return cars;
        }
    }

    @Override
    public List<Car> selectCarsByCompany(Company company) throws SQLException, ClassNotFoundException {
        List<Car> cars = new ArrayList<>();
        String sql = String.format("SELECT ID, NAME, COMPANY_ID " +
                                   "FROM CAR " +
                                   "WHERE COMPANY_ID = %d", company.getId());
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyID = resultSet.getInt("COMPANY_ID");
                cars.add(new Car(id, name, companyID));
            }
            return cars;
        }
    }

    @Override
    public List<Car> selectNotRentedCarsByCompany(Company company) throws SQLException, ClassNotFoundException {
        List<Car> cars = new ArrayList<>();
        String sql = String.format("SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID " +
                                   "FROM CAR " +
                                   "LEFT JOIN CUSTOMER " +
                                   "ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                                   "WHERE CAR.COMPANY_ID = %d AND CUSTOMER.RENTED_CAR_ID IS NULL", company.getId());
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyID = resultSet.getInt("COMPANY_ID");
                cars.add(new Car(id, name, companyID));
            }
            return cars;
        }
    }
}
