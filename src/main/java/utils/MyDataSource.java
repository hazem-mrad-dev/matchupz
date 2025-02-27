package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataSource {
    private static MyDataSource instance;
    private Connection conn;

    private final String URL = "jdbc:mysql://localhost:3306/matchupz?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = ""; // No password, as per your setup

    private MyDataSource() {
        try {
            // Explicitly load the MySQL driver (optional for modern JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Failed to load MySQL driver", e);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static MyDataSource getInstance() {
        if (instance == null) {
            instance = new MyDataSource();
        }
        return instance;
    }

    public Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {
                System.err.println("Connection is null or closed. Re-establishing...");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connection re-established successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to re-establish connection: " + e.getMessage());
            throw new RuntimeException("Cannot re-establish database connection", e);
        }
        return conn;
    }

    // Optional: Close connection explicitly if needed
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }
}