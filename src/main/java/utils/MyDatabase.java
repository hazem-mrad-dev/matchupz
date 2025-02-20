package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/matchupz";
    private final String USER = "root";
    private final String PASSWORD = "";

    private Connection connection;

    private static MyDatabase instance;

    // Private constructor to prevent instantiation
    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Singleton method to get the instance
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is closed or null. Reinitializing connection.");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } else {
                System.out.println("Returning an active connection.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking or reinitializing the connection: " + e.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            } else {
                System.out.println("Connection was already closed or null.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
