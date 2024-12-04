package main.java.com.example.backend.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.example.backend.AuthResponse;
import main.java.com.example.backend.JWTService;
import main.java.com.example.backend.Password;
import main.java.com.example.backend.PasswordManager;
import main.java.com.example.backend.RegisterRequest;
import main.java.com.example.backend.User;
import main.java.com.example.backend.UserResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {


    
    @Autowired
    private Connection connection;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            boolean registered = User.registerUser(connection, request.username, request.email, request.password);
            if (registered) {
                User user = User.loginUser(connection, request.username, request.password);
                String token = JWTService.generateToken(user.getUserId());
                return ResponseEntity.ok(new AuthResponse(token, user));
            }
            return ResponseEntity.badRequest().body("Registration failed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        try {
            int userId = JWTService.validateToken(token.replace("Bearer ", ""));
            User user = new User(userId);
            List<Password> passwords = PasswordManager.getAllPasswords(connection, user);
            return ResponseEntity.ok(new UserResponse(user, passwords));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @DeleteMapping("/password/{website}")
    public ResponseEntity<?> deletePassword(@PathVariable String website, 
                                          @RequestHeader("Authorization") String token) {
        try {
            int userId = JWTService.validateToken(token.replace("Bearer ", ""));
            User user = new User(userId);
            PasswordManager.deletePassword(connection, website, user);
            return ResponseEntity.ok("Password deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/password/add")
    public ResponseEntity<?> addPassword(@RequestBody Password password,
                                       @RequestHeader("Authorization") String token) {
        try {
            int userId = JWTService.validateToken(token.replace("Bearer ", ""));
            User user = new User(userId);
            password.userID = userId;
            PasswordManager.addPassword(connection, password, user);
            return ResponseEntity.ok("Password added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/password/edit/{website}")
    public ResponseEntity<?> editPassword(@PathVariable String website,
                                        @RequestBody Password password,
                                        @RequestHeader("Authorization") String token) {
        try {
            int userId = JWTService.validateToken(token.replace("Bearer ", ""));
            User user = new User(userId);
            password.userID = userId;
            PasswordManager.editPassword(connection, user, website);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }
}
