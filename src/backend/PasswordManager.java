package backend;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.crypto.SecretKey;

public class PasswordManager {

    //Adds Password
    public static void addPassword(Connection connection, Password password, User user) {
        String query = "INSERT INTO Passwords (UserID, Website, Username, EncryptedPassword, SecurityQuestion) VALUES (?, ?, ?, ?, ?)";
        try {
            SecretKey secretKey = User.retrieveSecretKey(connection, user.getUserId());
            if (secretKey == null) {
                System.out.println("Error: Secret key not found for user.");
                return;
            }
            
            String encryptedPassword = Encryption.encrypt(password.getPlainPassword(), secretKey);
    
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, password.userID);
                statement.setString(2, password.website);
                statement.setString(3, password.username);
                statement.setString(4, encryptedPassword);
                statement.setString(5, password.securityQuestion);
                statement.executeUpdate();
                System.out.println("Password added successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error in password operation: " + e.getMessage());
        }
    }

    //Retrieves Password
    public static void retrievePassword(Connection connection, String website, User user) throws Exception {
        String query = "SELECT EncryptedPassword FROM Passwords WHERE UserID = ? AND Website = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.setString(2, website);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                String encryptedPassword = resultSet.getString("EncryptedPassword");
                
    
                SecretKey secretKey = User.retrieveSecretKey(connection, user.getUserId());
                if (secretKey == null) {
                    System.out.println("Error: Secret key not found for user.");
                    return;
                }
    
                try {
                    String decryptedPassword = Encryption.decrypt(encryptedPassword, secretKey);
                    System.out.println("----------------------------------------");
                    System.out.println("Password: " + decryptedPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No password found for website: " + website);
            }
        }
    }
    
    
    //Shows all passwords for the User
    public static void showAllPasswords(Connection connection, User user) {
        String query = "SELECT Website, Username, EncryptedPassword, SecurityQuestion FROM Passwords WHERE UserID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();
    
            SecretKey secretKey = User.retrieveSecretKey(connection, user.getUserId());
            if (secretKey == null) {
                System.out.println("Error: Secret key not found for user.");
                return;
            }
    
            System.out.println("All stored passwords:");
            System.out.println("---------------------------------------------------------");
    
            boolean hasPasswords = false;
    
            while (resultSet.next()) {
                hasPasswords = true;
                String website = resultSet.getString("Website");
                String username = resultSet.getString("Username");
                String encryptedPassword = resultSet.getString("EncryptedPassword");
                String securityQuestion = resultSet.getString("SecurityQuestion");
                String decryptedPassword;
                try {
                    decryptedPassword = Encryption.decrypt(encryptedPassword, secretKey);
                } catch (Exception e) {
                    decryptedPassword = "[Error decrypting password]";
                }
                System.out.println("Website: " + website);
                System.out.println("Username: " + username);
                System.out.println("Password: " + decryptedPassword);
                System.out.println("Security Question: " + securityQuestion);
                System.out.println("---------------------------------------------------------");
            }
    
            if (!hasPasswords) {
                System.out.println("No passwords stored for this user.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving passwords: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error decrypting passwords: " + e.getMessage());
        }
    }

    //Delete password
    public static void deletePassword(Connection connection, String website, User user) {
        String query = "DELETE FROM Passwords WHERE UserID = ? AND Website = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.setString(2, website);
    
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password for website '" + website + "' has been deleted successfully.");
            } else {
                System.out.println("No password found for website: '" + website + "' or it does not belong to the user.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting password: " + e.getMessage());
        }
    }
    
    //Eidts Password needs some adjustments and fixes
    public static void editPassword(Connection connection, User user, String website) {
        Scanner scanner = new Scanner(System.in);

        String query = "SELECT * FROM Passwords WHERE UserID = ? AND Website = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.setString(2, website);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                System.out.println("Current Information for Website: " + website);
                System.out.println("1. Website: " + resultSet.getString("Website"));
                System.out.println("2. Username: " + resultSet.getString("Username"));
                System.out.println("3. Password: [Encrypted]");
                System.out.println("4. Security Question: " + resultSet.getString("SecurityQuestion"));


                System.out.println("\nWhich field would you like to edit?");
                System.out.println("1. Website");
                System.out.println("2. Username");
                System.out.println("3. Password");
                System.out.println("4. Security Question");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                String newValue = null;
                switch (choice) {
                    case 1:
                        System.out.print("Enter the new website: ");
                        newValue = scanner.nextLine();
                        updatePasswordField(connection, user, website, "Website", newValue);
                        break;

                    case 2:
                        System.out.print("Enter the new username: ");
                        newValue = scanner.nextLine();
                        updatePasswordField(connection, user, website, "Username", newValue);
                        break;

                    case 3:
                        System.out.print("Enter the new password: ");
                        newValue = scanner.nextLine();
                        SecretKey secretKey = User.retrieveSecretKey(connection, user.getUserId());
                        if (secretKey != null) {
                            String encryptedPassword = Encryption.encrypt(newValue, secretKey);
                            updatePasswordField(connection, user, website, "EncryptedPassword", encryptedPassword);
                        } else {
                            System.out.println("Error: Secret key not found for user.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter the new security question: ");
                        newValue = scanner.nextLine();
                        updatePasswordField(connection, user, website, "SecurityQuestion", newValue);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("No password found for the website: " + website);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error encrypting password: " + e.getMessage());
        }

        scanner.close();
    }

    //Helper editpassword method to update password field
    private static void updatePasswordField(Connection connection, User user, String website, String column, String newValue) {
        String updateQuery = "UPDATE Passwords SET " + column + " = ? WHERE UserID = ? AND Website = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, newValue);
            statement.setInt(2, user.getUserId());
            statement.setString(3, website);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(column + " updated successfully.");
            } else {
                System.out.println("Failed to update the password record.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating password field: " + e.getMessage());
        }
    }

    //sorts passwords
    public static void sortPasswords(Connection connection, User user, String sortBy) {
        //retrieve all passwords
        List<Password> passwords = new ArrayList<>();
        String query = "SELECT Website, Username, EncryptedPassword, SecurityQuestion FROM Passwords WHERE UserID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();
    
            
            SecretKey secretKey = User.retrieveSecretKey(connection, user.getUserId());
            if (secretKey == null) {
                System.out.println("Error: Secret key not found for user.");
                return;
            }
    
            while (resultSet.next()) {
                try {
                    String website = resultSet.getString("Website");
                    String username = resultSet.getString("Username");
                    String encryptedPassword = resultSet.getString("EncryptedPassword");
                    String securityQuestion = resultSet.getString("SecurityQuestion");
                    
                  
                    Password password = new Password(
                        user.getUserId(),
                        website,
                        username,
                        Encryption.decrypt(encryptedPassword, secretKey),
                        securityQuestion,
                        secretKey
                    );
                    passwords.add(password);
                } catch (Exception e) {
                    System.out.println("Error processing password: " + e.getMessage());
                }
            }
    
            switch (sortBy.toLowerCase()) {
                case "website":
                    Collections.sort(passwords, Comparator.comparing(Password::getWebsite));
                    break;
                case "username":
                    Collections.sort(passwords, Comparator.comparing(Password::getUsername));
                    break;
                default:
                    System.out.println("Invalid sort criteria. Valid options are: website, username");
                    return;
            }
            System.out.println("\nSorted passwords by " + sortBy + ":");
            System.out.println("---------------------------------------------------------");
            for (Password password : passwords) {
                System.out.println("Website: " + password.getWebsite());
                System.out.println("Username: " + password.getUsername());
                System.out.println("Password: " + password.getPlainPassword());
                System.out.println("Security Question: " + password.getSecurityQuestion());
                System.out.println("---------------------------------------------------------");
            }
    
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error processing passwords: " + e.getMessage());
        }
    }

    public static void listCompromisedPasswords(Connection connection, User user) {
        String query = "SELECT Website, EncryptedPassword FROM Passwords WHERE UserID = ?";
        boolean foundCompromised = false;
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();
    
            SecretKey secretKey = User.retrieveSecretKey(connection, user.getUserId());
            if (secretKey == null) {
                System.out.println("Error: Secret key not found for user.");
                return;
            }
    
            System.out.println("compromised passwords");
            System.out.println("---------------------------------------------------------");
    
            while (resultSet.next()) {
                String website = resultSet.getString("Website");
                String encryptedPassword = resultSet.getString("EncryptedPassword");
                
                try {
                    String decryptedPassword = Encryption.decrypt(encryptedPassword, secretKey);
                    if (Password.isPasswordCompromised(decryptedPassword)) {
                        foundCompromised = true;
                        System.out.println("Compromised password found");
                        System.out.println("Website: " + website);
                        System.out.println("---------------------------------------------------------");
                    }
                } catch (Exception e) {
                    System.out.println("Error checking password for website " + website + ": " + e.getMessage());
                }
            }
    
            if (!foundCompromised) {
                System.out.println("No compromised passwords found.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error processing passwords: " + e.getMessage());
        }
    }

}
