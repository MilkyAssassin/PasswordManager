
/*
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.sql.Connection;
import javax.crypto.SecretKey;
import java.util.List;

import main.java.com.example.backend.*;

public class PasswordManagerTest {
    private Connection connection;
    private User testUser;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "Test123!";
    private static final String TEST_WEBSITE = "testsite.com";

    @Before
    public void setUp() throws Exception {
        DatabaseConnection.initializeDatabase();
        connection = DatabaseConnection.getConnection();
        // Create test user
        testUser = new User(TEST_USERNAME, TEST_PASSWORD);
        testUser.register(connection);
        testUser = User.login(connection, TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    public void testAddPassword() throws Exception {
        // Test adding a new password
        Password password = new Password(
            testUser.getUserId(),
            TEST_WEBSITE,
            "username1",
            "password123",
            "What is your pet's name?",
            User.retrieveSecretKey(connection, testUser.getUserId())
        );
        
        PasswordManager.addPassword(connection, password, testUser);
        
        // Verify password was added
        List<Password> passwords = PasswordManager.getAllPasswords(connection, testUser);
        boolean found = passwords.stream()
            .anyMatch(p -> p.getWebsite().equals(TEST_WEBSITE));
        assertTrue("Password should be added successfully", found);
    }

    @Test
    public void testRetrievePassword() throws Exception {
        // Add a password first
        Password password = new Password(
            testUser.getUserId(),
            TEST_WEBSITE,
            "username1",
            "password123",
            "What is your pet's name?",
            User.retrieveSecretKey(connection, testUser.getUserId())
        );
        PasswordManager.addPassword(connection, password, testUser);
        
        // Test retrieval
        List<Password> passwords = PasswordManager.getAllPasswords(connection, testUser);
        Password retrieved = passwords.stream()
            .filter(p -> p.getWebsite().equals(TEST_WEBSITE))
            .findFirst()
            .orElse(null);
            
        assertNotNull("Password should be retrieved", retrieved);
        assertEquals("password123", retrieved.getPlainPassword());
    }

    @Test
    public void testEditPassword() throws Exception {
        // Add initial password
        Password password = new Password(
            testUser.getUserId(),
            TEST_WEBSITE,
            "username1",
            "password123",
            "What is your pet's name?",
            User.retrieveSecretKey(connection, testUser.getUserId())
        );
        PasswordManager.addPassword(connection, password, testUser);
        
        // Update the password
        String newUsername = "newUsername";
        PasswordManager.updatePasswordField(connection, testUser, TEST_WEBSITE, "Username", newUsername);
        
        // Verify update
        List<Password> passwords = PasswordManager.getAllPasswords(connection, testUser);
        Password updated = passwords.stream()
            .filter(p -> p.getWebsite().equals(TEST_WEBSITE))
            .findFirst()
            .orElse(null);
            
        assertNotNull("Updated password should exist", updated);
        assertEquals("Username should be updated", newUsername, updated.getUsername());
    }

    @Test
    public void testDeletePassword() throws Exception {
        // Add a password
        Password password = new Password(
            testUser.getUserId(),
            TEST_WEBSITE,
            "username1",
            "password123",
            "What is your pet's name?",
            User.retrieveSecretKey(connection, testUser.getUserId())
        );
        PasswordManager.addPassword(connection, password, testUser);
        
        // Delete the password
        PasswordManager.deletePassword(connection, TEST_WEBSITE, testUser);
        
        // Verify deletion
        List<Password> passwords = PasswordManager.getAllPasswords(connection, testUser);
        boolean found = passwords.stream()
            .anyMatch(p -> p.getWebsite().equals(TEST_WEBSITE));
        assertFalse("Password should be deleted", found);
    }

    @Test
    public void testListCompromisedPasswords() throws Exception {
        // Add a compromised password (assuming "password123" is in compromised list)
        Password password = new Password(
            testUser.getUserId(),
            TEST_WEBSITE,
            "username1",
            "password123",
            "What is your pet's name?",
            User.retrieveSecretKey(connection, testUser.getUserId())
        );
        PasswordManager.addPassword(connection, password, testUser);
        
        // Test will pass if no exceptions are thrown
        PasswordManager.listCompromisedPasswords(connection, testUser);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up test data
        String query = "DELETE FROM Passwords WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, testUser.getUserId());
            stmt.executeUpdate();
        }
        
        query = "DELETE FROM Users WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }
        
        connection.close();
    }
}
    */