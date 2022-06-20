package com.pavelmuravyev.carsharing.dao;

import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Company;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDao {

    void createTableIfNotExists() throws SQLException, ClassNotFoundException;
    void insertCompany(String name) throws SQLException, ClassNotFoundException;
    Company selectCompanyByCar(Car car) throws SQLException, ClassNotFoundException;
    List<Company> selectAllCompanies() throws SQLException, ClassNotFoundException;
}
