package main.java.com.example.backend.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.example.backend.DatabaseConnection;
import main.java.com.example.backend.Password;
import main.java.com.example.backend.PasswordManager;
import main.java.com.example.backend.User;
import main.java.com.example.backend.dto.ApiResponse;
import main.java.com.example.backend.dto.PasswordRequest;

@RestController
@RequestMapping("/passwords")
public class PasswordController {

    @PostMapping("/add")
    public ResponseEntity<?> addPassword(@RequestBody PasswordRequest request) {
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
    public ResponseEntity<?> deletePassword(@PathVariable String website, @RequestParam int userId) {
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
    public ResponseEntity<?> getAllPasswords(@PathVariable int userId) {
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
}
