package com.pavelmuravyev.carsharing;

import com.pavelmuravyev.carsharing.db.H2DataBase;
import com.pavelmuravyev.carsharing.service.AppManager;

public class Main {

    public static void main(String[] args) {
        String dbName = "carsharing";
        H2DataBase dataBase = new H2DataBase(dbName);
        AppManager appManager = new AppManager();
        appManager.start(dataBase);
    }
}
