package backend;

import java.sql.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class User {
    public static int userId;
    public String username;
    public String email;
    public String password;
    

    // Constructor
    public User(int userId, String username, String email, String password) {
        User.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
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
        String query = "INSERT INTO Users (Username, Email, PasswordHash, SecretKey) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            SecretKey secretKey = Encryption.genSecretKey();
            byte[] secretKeyBytes = secretKey.getEncoded();
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setBytes(4, secretKeyBytes);
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
        String query = "SELECT * FROM Users WHERE Username = ? AND PasswordHash = ?";
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

        String query = "SELECT SecretKey FROM users WHERE UserID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    byte[] keyBytes = rs.getBytes("SecretKey");
                    

                    SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
                    return secretKey;
                } else {
                    throw new SQLException("No secret key found for user with ID: " + userId);
                }
            }
        }
    }

}
