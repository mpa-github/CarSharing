package com.pavelmuravyev.carsharing.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DataBase {

    private static final String PATH_SEPARATOR = File.separator;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String H2_JDBC_URL = "jdbc:h2:." + PATH_SEPARATOR +
                                              "src" + PATH_SEPARATOR +
                                              "main" + PATH_SEPARATOR +
                                              "java" + PATH_SEPARATOR;
    private final String dbName;


    public H2DataBase(String dbName) {
        this.dbName = dbName;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        Connection connection = DriverManager.getConnection(getDbURL());
        connection.setAutoCommit(true);
        return connection;
    }

    private String getDbURL() {
        return H2_JDBC_URL +
               this.getClass()
                   .getPackage()
                   .getName()
                   .replace(".", PATH_SEPARATOR) +
               PATH_SEPARATOR + dbName;
    }
}
