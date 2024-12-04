
package com.example.backend.controller;

import com.example.backend.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.UserRegistrationRequest;
import com.example.backend.model.ApiResponse;
import com.example.backend.User;
import com.example.backend.DatabaseConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
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