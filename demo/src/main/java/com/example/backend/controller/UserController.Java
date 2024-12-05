
package main.java.com.example.backend.controller;

import main.java.com.example.backend.DatabaseConnection;
import main.java.com.example.backend.User;
import main.java.com.example.backend.dto.ApiResponse;
import main.java.com.example.backend.dto.UserLoginRequest;
import main.java.com.example.backend.dto.UserRegistrationRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            boolean success = User.registerUser(
                DatabaseConnection.getConnection(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
            );
            return ResponseEntity.ok().body(new ApiResponse(success, "User registration " + (success ? "successful" : "failed")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        try {
            User user = User.loginUser(
                DatabaseConnection.getConnection(),
                request.getUsername(),
                request.getPassword()
            );
            if (user != null) {
                return ResponseEntity.ok().body(user);
            }
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}