package com.crimedetect.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = ConfigUtils.getDatabaseUrl();
    private static final String USER = ConfigUtils.getDatabaseUsername();
    private static final String PASSWORD = ConfigUtils.getDatabasePassword();

    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Test connection (optional)
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        System.out.println("URL: " + URL);
        System.out.println("User: " + USER);
        
        try (Connection conn = getConnection()) {
            System.out.println("✓ Connected to database successfully!");
            System.out.println("Database: " + conn.getCatalog());
            System.out.println("Driver: " + conn.getMetaData().getDriverName());
            System.out.println("Version: " + conn.getMetaData().getDriverVersion());
        } catch (SQLException e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            System.err.println("\nTroubleshooting tips:");
            System.err.println("1. Ensure MySQL server is running");
            System.err.println("2. Check if database 'crimedb' exists");
            System.err.println("3. Verify username and password in application.properties");
            System.err.println("4. Run database_schema.sql to create required tables");
        }
    }
}