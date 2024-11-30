package backend;

import java.sql.*;
import javax.crypto.*;



public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    

    // Constructor
    public User(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
          // Store the secret key
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }





    // User Registration
    public static boolean registerUser(Connection connection, String username, String email, String password) throws Exception {
        String query = "INSERT INTO Users (Username, Email, password, SecretKey) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            SecretKey secretKey = Encryption.genSecretKey();
            String encryptedKey = Encryption.encodeSecretKey(secretKey);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, encryptedKey);
            statement.executeUpdate();
            System.out.println("User registered successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error during user registration: " + e.getMessage());
            return false;
        }
    }
    // User Login
    public static User loginUser(Connection connection, String username, String password) {
        String query = "SELECT * FROM Users WHERE Username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("UserID");
                String email = resultSet.getString("Email");
                System.out.println("Login successful.");
                return new User(userId, username, email, password);
            } else {
                System.out.println("Invalid username or password.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error during user login: " + e.getMessage());
            return null;
        }
    }

    
    
    public static SecretKey retrieveSecretKey(Connection connection, int userId) throws Exception {
        String query = "SELECT SecretKey FROM Users WHERE UserID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String encodedKey = resultSet.getString("SecretKey");
                return Encryption.decodeSecretKey(encodedKey);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving secret key: " + e.getMessage());
        }
        return null; 
    }
    




    
}
