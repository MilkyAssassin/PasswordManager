package backend;

import java.sql.*;

public class DatabaseConnection {
    private static final String url = "jdbc:sqlserver://database-1.cnqc6gewutb2.us-east-2.rds.amazonaws.com:1433;trustServerCertificate=true";
    private static final String user = "password_db";
    private static final String password = "Nx5g6upxroVDi5C";

    private static Connection connection = null;

    public static Connection getConnection() {//Connecting to database
        if (connection == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established.");
                useDatabase(connection);
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Failed to establish a database connection.");
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static void initializeDatabase() {//Connecting to database on app startup
        getConnection(); 
    }

    public static void closeConnection() {//Close connection to database
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }
    
    public static void shutdownDatabase() {// Stop connection on app shutdown
        closeConnection();
    }

    private static void useDatabase(Connection connection) {//Making sure correct database(pmanager) is being used
        String useDatabaseQuery = "USE pmanager";
        try (Statement statement = connection.createStatement()) {
            statement.execute(useDatabaseQuery);
            System.out.println("Switched to database: pmanager.");
        } catch (SQLException e) {
            System.err.println("Failed to switch to the pmanager database.");
            e.printStackTrace();
        }
    }

}
