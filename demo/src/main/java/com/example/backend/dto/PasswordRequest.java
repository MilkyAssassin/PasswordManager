// src/main/java/com/example/backend/dto/PasswordRequest.java
package main.java.com.example.backend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PasswordRequest {
    @NotNull
    private int userId;

    @NotBlank(message = "Website is required")
    private String website;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String securityQuestion;

    // Default constructor
    public PasswordRequest() {}

    // Constructor with fields
    public PasswordRequest(int userId, String website, String username, String password, String securityQuestion) {
        this.userId = userId;
        this.website = website;
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    @Override
    public String toString() {
        return "PasswordRequest{" +
            "userId=" + userId +
            ", website='" + website + '\'' +
            ", username='" + username + '\'' +
            ", securityQuestion='" + securityQuestion + '\'' +
            // Password intentionally excluded for security
            '}';
    }
}