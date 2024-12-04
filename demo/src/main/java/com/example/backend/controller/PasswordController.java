// src/main/java/com/example/backend/controller/PasswordController.java
package main.java.com.example.backend.controller;

import main.java.com.example.backend.DatabaseConnection;
import main.java.com.example.backend.Password;
import main.java.com.example.backend.PasswordManager;
import main.java.com.example.backend.User;
import main.java.com.example.backend.dto.ApiResponse;
import main.java.com.example.backend.dto.PasswordRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passwords")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
public class PasswordController {

    @PostMapping
    public ResponseEntity<?> addPassword(@RequestBody PasswordRequest request) {
        try {
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPasswords(@PathVariable int userId) {
        try {
            return ResponseEntity.ok().body(
                PasswordManager.getAllPasswords(DatabaseConnection.getConnection(), new User(userId))
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/{website}")
    public ResponseEntity<?> deletePassword(@PathVariable int userId, @PathVariable String website) {
        try {
            PasswordManager.deletePassword(DatabaseConnection.getConnection(), website, new User(userId));
            return ResponseEntity.ok().body(new ApiResponse(true, "Password deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}