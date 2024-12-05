package main.java.com.example.backend;


import java.util.List;

public class UserResponse {
    public User user;
    public List<Password> passwords;
    
    public UserResponse(User user, List<Password> passwords) {
        this.user = user;
        this.passwords = passwords;
    }
}
