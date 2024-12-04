package main.java.com.example.backend;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/passwords")
public class PasswordController {
    
    @Autowired
    private Connection connection;
    
    @PostMapping("/add")
    public ResponseEntity<?> addPassword(@RequestBody Password password, @RequestHeader("userId") int userId) {
        try {
            User user = new User(userId);
            // Ensure the userId in path matches the password object
            password.userID = userId;
            PasswordManager.addPassword(connection, password, user);
            return ResponseEntity.ok("Password added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/retrieve/{website}")
    public ResponseEntity<?> getPassword(@PathVariable String website, @RequestHeader("userId") int userId) {
        try {
            User user = new User(userId);
            PasswordManager.retrievePassword(connection, website, user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
