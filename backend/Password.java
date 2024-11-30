package backend;

import javax.crypto.SecretKey;

public class Password {
    public String website;
    public String username;
    public String encryptedPassword; 
    public String securityQuestion;
    public int userID;
    public int passwordID;

    public Password(int userId, String website, String username, String password, String securityQuestion, SecretKey secretKey) throws Exception {
        this.userID = userId;
        this.website = website;
        this.username = username;
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

}



