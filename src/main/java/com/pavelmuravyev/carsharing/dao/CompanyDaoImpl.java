package com.pavelmuravyev.carsharing.dao;

import com.pavelmuravyev.carsharing.db.H2DataBase;
import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    private final H2DataBase db;

    public CompanyDaoImpl(H2DataBase db) {
        this.db = db;
    }

    @Override
    public void createTableIfNotExists() throws SQLException, ClassNotFoundException {
        String sql = "CREATE TABLE IF NOT EXISTS COMPANY" +
                     "(" +
                     "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                     "NAME VARCHAR NOT NULL UNIQUE" +
                     ")";
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public void insertCompany(String name) throws SQLException, ClassNotFoundException {
        String sql = String.format("INSERT INTO COMPANY (NAME) VALUES ('%s')", name);
        try (Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public Company selectCompanyByCar(Car car) throws SQLException, ClassNotFoundException {
        String sql = String.format("SELECT ID, NAME " +
                                   "FROM COMPANY " +
                                   "WHERE ID = %d", car.getCompanyID());
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            return new Company(id, name);
        }
    }

    @Override
    public List<Company> selectAllCompanies() throws SQLException, ClassNotFoundException {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT ID, NAME " +
                     "FROM COMPANY";
        try (Statement statement = db.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                companies.add(new Company(id, name));
            }
            return companies;
        }
    }
}
