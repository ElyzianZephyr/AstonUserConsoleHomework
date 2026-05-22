package org.userservice.console;


import org.userservice.entity.User;
import org.userservice.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    private final UserService userService;
    private final Scanner scanner;

    public ConsoleUI(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== User Management System ===");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createUser();
                case "2" -> findUserById();
                case "3" -> updateUser();
                case "4" -> deleteUser();
                case "5" -> listAllUsers();
                case "0" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Create user");
        System.out.println("2. Find user by ID");
        System.out.println("3. Update user");
        System.out.println("4. Delete user");
        System.out.println("5. List all users");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ==================== CREATE ====================

    private void createUser() {
        System.out.println("\n--- Create New User ---");

        String name = readName();
        int age = readAge();
        String email = readEmail();

        try {
            User user = userService.createUser(name, age, email);
            System.out.println("User created successfully!");
            printUser(user);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==================== FIND BY ID ====================

    private void findUserById() {
        System.out.println("\n--- Find User by ID ---");

        Long id = readId();
        if (id == null) return;

        Optional<User> userOpt = userService.findUser(id);

        if (userOpt.isPresent()) {
            System.out.println("User found:");
            printUser(userOpt.get());
        } else {
            System.out.println("User with ID=" + id + " not found.");
        }
    }

    // ==================== UPDATE ====================

    private void updateUser() {
        System.out.println("\n--- Update User ---");

        Long id = readId();
        if (id == null) return;

        Optional<User> userOpt = userService.findUser(id);
        if (userOpt.isEmpty()) {
            System.out.println("User with ID=" + id + " not found.");
            return;
        }

        User user = userOpt.get();
        System.out.println("\nCurrent user data:");
        printUser(user);

        boolean updating = true;
        while (updating) {
            printUpdateMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    String newName = readName();
                    updateUserField(id, newName, user.getAge(), user.getEmail());
                    updating = false;
                }
                case "2" -> {
                    int newAge = readAge();
                    updateUserField(id, user.getName(), newAge, user.getEmail());
                    updating = false;
                }
                case "3" -> {
                    String newEmail = readEmail();
                    updateUserField(id, user.getName(), user.getAge(), newEmail);
                    updating = false;
                }
                case "0" -> {
                    System.out.println("Update cancelled.");
                    updating = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void printUpdateMenu() {
        System.out.println("\nWhat do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Email");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");
    }

    private void updateUserField(Long id, String name, int age, String email) {
        try {
            User updated = userService.updateUser(id, name, age, email);
            System.out.println("User updated successfully!");
            printUser(updated);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==================== DELETE ====================

    private void deleteUser() {
        System.out.println("\n--- Delete User ---");

        Long id = readId();
        if (id == null) return;

        Optional<User> userOpt = userService.findUser(id);
        if (userOpt.isEmpty()) {
            System.out.println("User with ID=" + id + " not found.");
            return;
        }

        System.out.println("User to delete:");
        printUser(userOpt.get());

        if (confirmAction("Are you sure you want to delete this user? (y/n): ")) {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==================== LIST ALL ====================

    private void listAllUsers() {
        System.out.println("\n--- All Users ---");

        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Total users: " + users.size());
        for (User user : users) {
            printSeparator();
            printUser(user);
        }
        printSeparator();
    }

    // ==================== INPUT METHODS ====================

    private Long readId() {
        while (true) {
            System.out.print("Enter user ID (or 0 to cancel): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) return null;

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a number.");
            }
        }
    }

    private String readName() {
        while (true) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
                continue;
            }
            return name;
        }
    }

    private int readAge() {
        while (true) {
            System.out.print("Enter age: ");
            String input = scanner.nextLine().trim();

            try {
                int age = Integer.parseInt(input);
                if (age <= 0 || age > 150) {
                    System.out.println("Age must be between 1 and 150. Please try again.");
                    continue;
                }
                return age;
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a number.");
            }
        }
    }

    private String readEmail() {
        while (true) {
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email cannot be empty. Please try again.");
                continue;
            }
            if (!email.contains("@")) {
                System.out.println("Email must contain '@'. Please try again.");
                continue;
            }
            return email;
        }
    }

    private boolean confirmAction(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    // ==================== OUTPUT FORMATTING ====================

    private void printUser(User user) {
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Age: " + user.getAge());
        System.out.println("Email: " + user.getEmail());
    }

    private void printSeparator() {
        System.out.println("-------------------");
    }
}