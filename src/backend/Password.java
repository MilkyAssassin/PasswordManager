package backend;

import javax.crypto.SecretKey;

public class Password {
    public String website;
    public String username;
    public String encryptedPassword; 
    private String plainPassword;
    public String securityQuestion;
    public int userID;
    public int passwordID;

    public Password(int userId, String website, String username, String password, String securityQuestion, SecretKey secretKey) throws Exception {
        this.userID = userId;
        this.website = website;
        this.username = username;
        this.plainPassword = password;
        this.encryptedPassword = Encryption.encrypt(password, secretKey);
        this.securityQuestion = securityQuestion;
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

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPasswordID() {
        return passwordID;
    }

    public void setPasswordID(int passwordID) {
        this.passwordID = passwordID;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public static boolean isPasswordCompromised(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    
        try {
            HIBPClient client = new HIBPClient("PasswordManager");
            
            boolean isNotCompromised = client.check(password);
            
            return !isNotCompromised;
            
        } catch (CheckPasswordException e) {
            System.err.println("Error checking password: " + e.getMessage());
            throw new RuntimeException("Failed to check password security", e);
        }
    }

}



