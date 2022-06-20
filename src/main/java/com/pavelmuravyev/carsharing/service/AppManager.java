package com.pavelmuravyev.carsharing.service;

import com.pavelmuravyev.carsharing.MenuList;
import com.pavelmuravyev.carsharing.dao.CarDaoImpl;
import com.pavelmuravyev.carsharing.dao.CompanyDaoImpl;
import com.pavelmuravyev.carsharing.dao.CustomerDaoImpl;
import com.pavelmuravyev.carsharing.db.H2DataBase;
import com.pavelmuravyev.carsharing.entity.Car;
import com.pavelmuravyev.carsharing.entity.Company;
import com.pavelmuravyev.carsharing.entity.Customer;
import com.pavelmuravyev.carsharing.entity.RentInfo;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AppManager {

    private static final String NEW_LINE = System.lineSeparator();
    private final PrintService printer = new PrintService();
    private final ConsoleHandler handler = new ConsoleHandler();
    private MenuList menuSelector;
    private Company companySelector;
    private Customer customerSelector;
    private CompanyDaoImpl companyDao;
    private CustomerDaoImpl customerDao;
    private CarDaoImpl carDao;

    public void start(H2DataBase dataBase) {
        companyDao = new CompanyDaoImpl(dataBase);
        carDao = new CarDaoImpl(dataBase);
        customerDao = new CustomerDaoImpl(dataBase);
        try {
            companyDao.createTableIfNotExists();
            carDao.createTableIfNotExists();
            customerDao.createTableIfNotExists();
        } catch (SQLException | ClassNotFoundException e) {
            printMessage("Database initialization error!");
            e.printStackTrace();
            handler.close();
            return;
        }
        menuSelector = MenuList.MAIN;
        while (menuSelector != MenuList.EXIT) {
            switch (menuSelector) {
                case MAIN:
                    runMainMenu();
                    break;
                case MANAGER:
                    runManagerMenu();
                    break;
                case COMPANY:
                    runCompanyMenu(companySelector);
                    break;
                 case CUSTOMER:
                    runCustomerMenu(customerSelector);
                    break;
                default:
                    printMessage("Menu selection error!");
                    menuSelector = MenuList.MAIN;
                    break;
            }
        }
    }

    private void runMainMenu() {
        printer.printMainMenu();
        String inputRegex = "\\s*[0123]\\s*";
        switch (handler.getMenuItem(inputRegex)) {
            case 1:
                menuSelector = MenuList.MANAGER;
                break;
            case 2:
                chooseCustomer().ifPresentOrElse(customer -> {
                    customerSelector = customer;
                    menuSelector = MenuList.CUSTOMER;
                }, () -> menuSelector = MenuList.MAIN);
                break;
            case 3:
                createCustomer();
                menuSelector = MenuList.MAIN;
                break;
            case 0:
                handler.close();
                menuSelector = MenuList.EXIT;
                printMessage("Goodbye!");
                break;
            default:
                printMessage("Item selection error!");
                menuSelector = MenuList.MAIN;
                break;
        }
    }

    private void runManagerMenu() {
        printer.printManagerMenu();
        String inputRegex = "\\s*[012]\\s*";
        switch (handler.getMenuItem(inputRegex)) {
            case 1:
                chooseCompany().ifPresentOrElse(company -> {
                    companySelector = company;
                    menuSelector = MenuList.COMPANY;
                }, () -> menuSelector = MenuList.MANAGER);
                break;
            case 2:
                createCompany();
                menuSelector = MenuList.MANAGER;
                break;
            case 0:
                menuSelector = MenuList.MAIN;
                break;
            default:
                printMessage("Item selection error!");
                menuSelector = MenuList.MANAGER;
                break;
        }
    }

    private void runCompanyMenu(Company company) {
        printer.printCompanyMenu(company.getName());
        String inputRegex = "\\s*[012]\\s*";
        switch (handler.getMenuItem(inputRegex)) {
            case 1:
                displayCarListByCompany(company);
                menuSelector = MenuList.COMPANY;
                break;
            case 2:
                createCar(company);
                menuSelector = MenuList.COMPANY;
                break;
            case 0:
                companySelector = null;
                menuSelector = MenuList.MANAGER;
                break;
            default:
                printMessage("Item selection error!");
                menuSelector = MenuList.COMPANY;
                break;
        }
    }

    private void runCustomerMenu(Customer customer) {
        printer.printCustomerMenu(customer.getName());
        String inputRegex = "\\s*[0123]\\s*";
        switch (handler.getMenuItem(inputRegex)) {
            case 1:
                rentCar(customer);
                menuSelector = MenuList.CUSTOMER;
                break;
            case 2:
                returnCar(customer);
                menuSelector = MenuList.CUSTOMER;
                break;
            case 3:
                displayRentedCar(customer);
                menuSelector = MenuList.CUSTOMER;
                break;
            case 0:
                customerSelector = null;
                menuSelector = MenuList.MAIN;
                break;
            default:
                printMessage("Item selection error!");
                menuSelector = MenuList.MAIN;
                break;
        }
    }

    private Optional<Company> chooseCompany() {
        try {
            List<Company> companies = companyDao.selectAllCompanies();
            if (!companies.isEmpty()) {
                Map<Integer, String> listToChoose = getNumberedCompanyNameMap(companies);
                listToChoose.put(0, "Back");
                printer.printNumberedList(listToChoose, "Choose a company:");
                int userChoice = handler.getMenuItem(listToChoose.keySet());
                if (userChoice != 0) {
                    return companies.stream()
                                    .filter(company -> listToChoose.get(userChoice).equals(company.getName()))
                                    .findAny();
                } else {
                    return Optional.empty();
                }
            } else {
                printMessage("The company list is empty!");
                return Optional.empty();
            }
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<Customer> chooseCustomer() {
        try {
            List<Customer> customers = customerDao.selectAllCustomers();
            if (!customers.isEmpty()) {
                Map<Integer, String> listToChoose = getNumberedCustomerNameMap(customers);
                listToChoose.put(0, "Back");
                printer.printNumberedList(listToChoose, "Choose a customer:");
                int userChoice = handler.getMenuItem(listToChoose.keySet());
                if (userChoice != 0) {
                    return customers.stream()
                                    .filter(customer -> listToChoose.get(userChoice).equals(customer.getName()))
                                    .findAny();
                } else {
                    return Optional.empty();
                }
            } else {
                printMessage("The customer list is empty!");
                return Optional.empty();
            }
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
            return Optional.empty();
        }
    }

    private void displayCarListByCompany(Company company) {
        try {
            List<Car> cars = carDao.selectCarsByCompany(company);
            if (!cars.isEmpty()) {
                printer.printNumberedList(getNumberedCarNameMap(cars), "Car list:");
            } else {
                printMessage("The car list is empty!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
        }
    }

    private void rentCar(Customer customer) {
        if (customer.getCarID() != 0) {
            printMessage("You've already rented a car!");
            return;
        }
        Optional<Company> company = chooseCompany();
        if (company.isPresent()) {
            try {
                List<Car> cars = carDao.selectNotRentedCarsByCompany(company.get());
                if (cars.isEmpty()) {
                    printMessage(String.format("No available cars in the '%s' company.", company.get().getName()));
                    return;
                }
                Map<Integer, String> listToChoose = getNumberedCarNameMap(cars);
                listToChoose.put(0, "Back");
                printer.printNumberedList(listToChoose, "Choose a car:");
                int userChoice = handler.getMenuItem(listToChoose.keySet());
                if (userChoice != 0) {
                    Optional<Car> optCarToRent;
                    optCarToRent = cars.stream()
                                       .filter(car -> listToChoose.get(userChoice).equals(car.getName()))
                                       .findAny();
                    if (optCarToRent.isPresent()) {
                        Car carToRent = optCarToRent.get();
                        customerDao.rentCar(customer, carToRent);
                        customerSelector.setCarID(carToRent.getId());
                        printMessage(String.format("You rented '%s'", carToRent.getName()));
                    } else {
                        printMessage(String.format("Car: '%s' not found in database!", listToChoose.get(userChoice)));
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                printMessage(e.getMessage());
            }
        }
    }

    private void displayRentedCar(Customer customer) {
        if (customer.getCarID() == 0) {
            printMessage("You didn't rent a car!");
            return;
        }
        try {
            RentInfo rentInfo = customerDao.selectRentInfo(customer);
            printMessage("Your rented car:" + NEW_LINE +
                         rentInfo.getCar().getName() + NEW_LINE +
                         "Company:" + NEW_LINE +
                         rentInfo.getCompany().getName());
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
        }
    }

    private void returnCar(Customer customer) {
        if (customer.getCarID() == 0) {
            printMessage("You didn't rent a car!");
            return;
        }
        try {
            customerDao.returnRentedCar(customer);
            customerSelector.setCarID(0);
            printMessage("You've returned a rented car!");
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
        }
    }

    private void createCompany() {
        printMessage("Enter the company name:");
        String nameRegex = "[a-zA-z \\d]*";
        String name = handler.getName(nameRegex);
        try {
            companyDao.insertCompany(name);
            printMessage("The company was created!");
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
        }
    }

    private void createCar(Company company) {
        printMessage("Enter the car name:");
        String inputRegex = "[a-zA-z \\d]*";
        String name = handler.getName(inputRegex);
        try {
            carDao.insertCar(name, company);
            printMessage("The car was added!");
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
        }
    }

    private void createCustomer() {
        printMessage("Enter the customer name:");
        String inputRegex = "[a-zA-z \\d]*";
        String name = handler.getName(inputRegex);
        try {
            customerDao.insertCustomer(name);
            printMessage("The customer was added!");
        } catch (SQLException | ClassNotFoundException e) {
            printMessage(e.getMessage());
        }
    }

    public Map<Integer, String> getNumberedCompanyNameMap(List<Company> companies) {
        Map<Integer, String> resultMap = new LinkedHashMap<>();
        AtomicInteger item = new AtomicInteger(1);
        companies.stream()
                 .sorted(Comparator.comparing(Company::getId))
                 .forEachOrdered(company -> resultMap.put(item.getAndIncrement(), company.getName()));
        return resultMap;
    }

    public Map<Integer, String> getNumberedCarNameMap(List<Car> cars) {
        Map<Integer, String> resultMap = new LinkedHashMap<>();
        AtomicInteger item = new AtomicInteger(1);
        cars.stream()
            .sorted(Comparator.comparing(Car::getId))
            .forEachOrdered(car -> resultMap.put(item.getAndIncrement(), car.getName()));
        return resultMap;
    }

    public Map<Integer, String> getNumberedCustomerNameMap(List<Customer> customers) {
        Map<Integer, String> resultMap = new LinkedHashMap<>();
        AtomicInteger item = new AtomicInteger(1);
        customers.stream()
                 .sorted(Comparator.comparing(Customer::getId))
                 .forEachOrdered(customer -> resultMap.put(item.getAndIncrement(), customer.getName()));
        return resultMap;
    }

    private void printMessage(String message) {
        printEmptyLine();
        System.out.println(message);
    }

    private void printEmptyLine() {
        System.out.println();
    }
}
