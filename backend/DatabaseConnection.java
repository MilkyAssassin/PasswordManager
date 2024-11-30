package backend;

import java.sql.*;

public class DatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://database-1.cnqc6gewutb2.us-east-2.rds.amazonaws.com:1433;trustServerCertificate=true";
        String user = "password_db";
        String password = "Nx5g6upxroVDi5C";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
            connection.close();
            
        }catch (ClassNotFoundException e) {

            e.printStackTrace();

        }catch (SQLException e) {

            e.printStackTrace();

        }
        
        
    }
}
