package main.java.com.example.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.SecretKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import main.java.com.example.backend.DatabaseConnection;
import main.java.com.example.backend.Encryption;
import main.java.com.example.backend.Password;
import main.java.com.example.backend.PasswordManager;
import main.java.com.example.backend.User;
import main.java.com.example.backend.dto.ApiResponse;
import main.java.com.example.backend.dto.PasswordRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // Allow all origins and headers
@RequestMapping("/passwords")
public class PasswordController {

    @PostMapping("/add")
    public ResponseEntity<?> addPassword(@RequestBody PasswordRequest request) { //Takes userud, username, password, website, security question
        try {
            if (!isUserAuthenticated(request.getUserId())) {
                return ResponseEntity.status(401).body(new ApiResponse(false, "User not authenticated"));
            }

            Password password = new Password(
                request.getUserId(),
                request.getWebsite(),
                request.getUsername(),
                request.getPassword(),
                request.getSecurityQuestion(),
                User.retrieveSecretKey(DatabaseConnection.getConnection(), request.getUserId())
            );
            
            PasswordManager.addPassword(DatabaseConnection.getConnection(), password, new User(request.getUserId()));
            return ResponseEntity.ok().body(new ApiResponse(true, "Password added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{website}")
    public ResponseEntity<?> deletePassword(@PathVariable String website, @RequestParam int userId) {//takes website and user id
        try {
            if (!isUserAuthenticated(userId)) {
                return ResponseEntity.status(401).body(new ApiResponse(false, "User not authenticated"));
            }

            User user = new User(userId);
            PasswordManager.deletePassword(DatabaseConnection.getConnection(), website, user);
            return ResponseEntity.ok().body(new ApiResponse(true, "Password deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPasswords(@PathVariable int userId) {//takes user id
        try {
            if (!isUserAuthenticated(userId)) {
                return ResponseEntity.status(401).body(new ApiResponse(false, "User not authenticated"));
            }

            User user = new User(userId);
            List<Password> passwords = PasswordManager.getAllPasswords(DatabaseConnection.getConnection(), user);
            return ResponseEntity.ok().body(passwords);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }






    private boolean isUserAuthenticated(int userId) {
        try {
        String query = "SELECT UserID FROM Users WHERE UserID = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            
            // Return true if user exists in database
            return resultSet.next();
        }
        } catch (SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
            return false;
        }
    }
    @PutMapping("/edit/{userId}")
    public ResponseEntity<?> editPassword(//takes user id, website, new website, new username, new password, new security question
            @PathVariable int userId,
            @RequestParam String website,
            @RequestParam(required = false) String newWebsite,
            @RequestParam(required = false) String newUsername,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String newSecurityQuestion) {
        try {
            if (!isUserAuthenticated(userId)) {
                return ResponseEntity.status(401).body(new ApiResponse(false, "User not authenticated"));
            }

            User user = new User(userId);
            Connection conn = DatabaseConnection.getConnection();

            // Verify password exists for this website
            String checkQuery = "SELECT * FROM Passwords WHERE UserID = ? AND Website = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
                stmt.setInt(1, userId);
                stmt.setString(2, website);
                if (!stmt.executeQuery().next()) {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "No password found for this website"));
                }
            }

            // Update fields that were provided
            if (newWebsite != null) {
                updatePasswordField(conn, user, website, "Website", newWebsite);
                website = newWebsite; // Update website reference for subsequent updates
            }
            if (newUsername != null) {
                updatePasswordField(conn, user, website, "Username", newUsername);
            }
            if (newPassword != null) {
                SecretKey secretKey = User.retrieveSecretKey(conn, userId);
                if (secretKey != null) {
                    String encryptedPassword = Encryption.encrypt(newPassword, secretKey);
                    updatePasswordField(conn, user, website, "EncryptedPassword", encryptedPassword);
                } else {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "Error: Secret key not found for user"));
                }
            }
            if (newSecurityQuestion != null) {
                updatePasswordField(conn, user, website, "SecurityQuestion", newSecurityQuestion);
            }

            return ResponseEntity.ok().body(new ApiResponse(true, "Password updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    
    private void updatePasswordField(Connection connection, User user, String website, String column, String newValue) throws SQLException {
        String updateQuery = "UPDATE Passwords SET " + column + " = ? WHERE UserID = ? AND Website = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, newValue);
            statement.setInt(2, user.getUserId());
            statement.setString(3, website);
            statement.executeUpdate();
        }
    }
}

