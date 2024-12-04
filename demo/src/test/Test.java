package test;



import java.sql.*;
import java.util.Scanner;

import javax.crypto.SecretKey;
import main.java.com.example.backend.DatabaseConnection;
import main.java.com.example.backend.Password;
import main.java.com.example.backend.PasswordManager;
import main.java.com.example.backend.User;


public class Test {
       public static void main(String[] args) throws Exception {
        // Start database connection
        DatabaseConnection.initializeDatabase();
        Connection connection = DatabaseConnection.getConnection();
        
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        User loggedInUser = null;

        while (isRunning) {
            System.out.println("\n========== Password Manager ==========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Add Password");
            System.out.println("4. Retrieve Password");
            System.out.println("5. Delete Password");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            System.out.println("8. Edit Password");
            System.out.println("9. Show All Passwords");
            System.out.println("10. Find Compromised Passwords");
            System.out.print("Enter your choice: ");
        
            int choice = scanner.nextInt();
            scanner.nextLine(); 
        
            switch (choice) {
                case 1:
                    // Register
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
        
                    if (User.registerUser(connection, username, email, password)) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed.");
                    }
                    break;
        
                case 2:
                    // Login
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
        
                    loggedInUser = User.loginUser(connection, loginUsername, loginPassword);
                    if (loggedInUser != null) {
                        System.out.println("Login successful! Welcome, " + loggedInUser.getUsername());
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;
        
                case 3:
                    // Add Password
                    if (loggedInUser != null) {
                        System.out.print("Enter website: ");
                        String website = scanner.nextLine();
                        System.out.print("Enter username for the website: ");
                        String siteUsername = scanner.nextLine();
                        System.out.println("\nPassword options:");
                        System.out.println("1. Generate secure password");
                        System.out.println("2. Enter password manually");
                        System.out.print("Choose option (1 or 2): ");
                        
                        String sitePassword;
                        String option = scanner.nextLine();
                        
                        if (option.equals("1")) {
                            sitePassword = Password.generateSecurePassword();
                            System.out.println("Generated password: " + sitePassword);
                        } else {
                            System.out.print("Enter password for the website: ");
                            sitePassword = scanner.nextLine();
                        }
                        System.out.print("Enter security question: ");
                        String securityQuestion = scanner.nextLine();
        
                        SecretKey secretKey = User.retrieveSecretKey(connection, loggedInUser.getUserId());
                        if (secretKey != null) {
                            Password passwordObj = new Password(loggedInUser.getUserId(), website, siteUsername, sitePassword, securityQuestion, secretKey);
                            PasswordManager.addPassword(connection, passwordObj, loggedInUser);
                        } else {
                            System.out.println("Error: Could not retrieve secret key for user.");
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
        
                case 4:
                    // Retrieve Password
                    if (loggedInUser != null) {
                        System.out.print("Enter website: ");
                        String retrieveWebsite = scanner.nextLine();
                        PasswordManager.retrievePassword(connection, retrieveWebsite, loggedInUser);
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
        
                case 5:
                    // Delete Password
                    if (loggedInUser != null) {
                        System.out.print("Enter website: ");
                        String deleteWebsite = scanner.nextLine();
                        PasswordManager.deletePassword(connection, deleteWebsite, loggedInUser);
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
        
                case 6:
                    // Logout
                    if (loggedInUser != null) {
                        System.out.println("Goodbye, " + loggedInUser.getUsername());
                        loggedInUser = null;
                    } else {
                        System.out.println("You are not logged in.");
                    }
                    break;
        
                case 7:
                    // Exit
                    isRunning = false;
                    break;
        
                case 8: 
                    // Edit Password Requires some adjustments
                    if (loggedInUser != null) {
                        System.out.print("Enter website for which you want to edit the password: ");
                        String editWebsite = scanner.nextLine();
                        PasswordManager.editPassword(connection, loggedInUser, editWebsite);
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 9:
                    // Show All Passwords and Sort
                    if (loggedInUser != null) {
                        boolean inShowPasswordsMenu = true;
                
                        while (inShowPasswordsMenu) {
                            System.out.println("\n===== Show Passwords Menu =====");
                            System.out.println("1. Show All Passwords");
                            System.out.println("2. Sort Passwords by Website");
                            System.out.println("3. Sort Passwords by Username");
                            System.out.println("4. Return to Main Menu");
                            System.out.print("Enter your choice: ");
                
                            int showMenuChoice = scanner.nextInt();
                            scanner.nextLine(); 
                
                            switch (showMenuChoice) {
                                case 1:
                                    // Show All Passwords
                                    PasswordManager.showAllPasswords(connection, loggedInUser);
                                    break;
                
                                case 2:
                                    // Sort Passwords by Website
                                    PasswordManager.sortPasswords(connection, loggedInUser, "website");
                                    break;
                
                                case 3:
                                    // Sort Passwords by Username
                                    PasswordManager.sortPasswords(connection, loggedInUser, "username");
                                    break;
                
                                case 4:
                                    // Exit Show Passwords Menu
                                    inShowPasswordsMenu = false;
                                    break;
                
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 10:
                    if (loggedInUser != null) {
                        PasswordManager.listCompromisedPasswords(connection, loggedInUser);
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
            }
        }

        // Close database connection
        DatabaseConnection.shutdownDatabase();
        scanner.close();
    }

}

