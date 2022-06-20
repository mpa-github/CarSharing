package com.pavelmuravyev.carsharing.service;

import java.util.Map;

public class PrintService {

    private static final String ITEM_SEPARATOR = ". ";

    public void printMainMenu() {
        printEmptyLine();
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    public void printManagerMenu() {
        printEmptyLine();
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    public void printCompanyMenu(String name) {
        printEmptyLine();
        System.out.println("'" + name + "' company:");
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    public void printCustomerMenu(String name) {
        printEmptyLine();
        System.out.println("'" + name + "' customer:");
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car ");
        System.out.println("0. Back");
    }

    public void printNumberedList(Map<Integer, String> numberedList, String caption) {
        printEmptyLine();
        System.out.println(caption);
        numberedList.forEach((itemNumber, name) -> System.out.println(itemNumber + ITEM_SEPARATOR + name));
    }

    private void printEmptyLine() {
        System.out.println();
    }
}
