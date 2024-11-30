package backend;

import java.sql.*;


public class Test {
    public static void main(String[] args) throws Exception {
        // Start database connection
        DatabaseConnection.initializeDatabase();
        System.out.println("Connected to Database.");

        Connection connection = DatabaseConnection.getConnection();     

        // Close database connection
        DatabaseConnection.shutdownDatabase();
        System.out.println("App closed.");
    }

}

