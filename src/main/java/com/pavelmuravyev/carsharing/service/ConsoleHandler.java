package com.pavelmuravyev.carsharing.service;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsoleHandler {

    private static final String SEVERAL_SPACES_REGEX = "\\s{2,}";
    private static final String SINGLE_SPACE = " ";
    private final Scanner scanner = new Scanner(System.in);

    public void close() {
        scanner.close();
    }

    public int getMenuItem(String itemRegex) {
        System.out.print("> ");
        String input = scanner.nextLine()
                              .replaceAll(SEVERAL_SPACES_REGEX, SINGLE_SPACE)
                              .trim();
        while (!input.matches(itemRegex)) {
            printEmptyLine();
            System.out.println("Incorrect item selection! Please try again.");
            System.out.print("> ");
            input = scanner.nextLine()
                           .replaceAll(SEVERAL_SPACES_REGEX, SINGLE_SPACE)
                           .trim();
        }
        return Integer.parseInt(input);
    }

    public int getMenuItem(Set<Integer> validItems) {
        List<String> items = validItems.stream()
                                       .map(String::valueOf)
                                       .collect(Collectors.toList());
        System.out.print("> ");
        String input = scanner.nextLine()
                              .replaceAll(SEVERAL_SPACES_REGEX, SINGLE_SPACE)
                              .trim();
        while (!items.contains(input)) {
            printEmptyLine();
            System.out.println("Incorrect item selection! Please try again.");
            System.out.print("> ");
            input = scanner.nextLine()
                           .replaceAll(SEVERAL_SPACES_REGEX, SINGLE_SPACE)
                           .trim();
        }
        return Integer.parseInt(input);
    }

    public String getName(String nameRegex) {
        System.out.print("> ");
        String input = scanner.nextLine()
                              .replaceAll(SEVERAL_SPACES_REGEX, SINGLE_SPACE)
                              .trim();
        while (!input.matches(nameRegex)) {
            printEmptyLine();
            System.out.println("Incorrect input! Please try again.");
            System.out.println("Use only \"a-Z\" letters and \"0-9\" digits.");
            System.out.print("> ");
            input = scanner.nextLine()
                           .replaceAll(SEVERAL_SPACES_REGEX, SINGLE_SPACE)
                           .trim();
        }
        return input;
    }

    private void printEmptyLine() {
        System.out.println();
    }
}
