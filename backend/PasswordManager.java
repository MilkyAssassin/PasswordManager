package backend;

import java.sql.*;

import javax.crypto.SecretKey;

public class PasswordManager {
    
    public static void addPassword(Connection connection, Password password){
        String query = "INSERT INTO Passwords (UserID, Website, Username, EncryptedPassword, SecurityQuestion) VALUES (?, ?, ?, ?, ?)";
        try {
        SecretKey secretKey = User.retrieveSecretKey(connection, password.userID);
        if (secretKey == null) {
            System.out.println("Error: Secret key not found for user.");
            return;
        }
        String encryptedPassword = Encryption.encrypt(password.encryptedPassword, secretKey);
        // Insert the password into the database 
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, password.userID);
            statement.setString(2, password.website);
            statement.setString(3, password.username);
            statement.setString(4, encryptedPassword);
            statement.setString(5, password.securityQuestion);
            statement.executeUpdate();
            System.out.println("Password added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding password: " + e.getMessage());
        }
    } catch (Exception e) {
        System.out.println("Error encrypting password: " + e.getMessage());
    }
    }

    public static void deletePassword(Connection connection, Password password){

    }

    public static void editPassword(){

    }

    public static void sortPasswords(){
        
    }
}
