package main.java.com.example.backend;

public class AuthResponse {
    public String token;
    public User user;
    
    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
