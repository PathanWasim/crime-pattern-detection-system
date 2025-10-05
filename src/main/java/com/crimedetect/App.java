package com.crimedetect;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.crimedetect.auth.LoginFrame;
import com.crimedetect.utils.DatabaseConnection;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Crime Detection System - Main Application Entry Point
 * 
 * This application provides a comprehensive crime management system
 * with features for recording, tracking, and analyzing crime data.
 * 
 * Features:
 * - User authentication
 * - Crime record management
 * - Advanced search and filtering
 * - Data visualization and analytics
 * - Report generation and export
 * - Evidence and case update tracking
 * 
 * @author Crime Detection System Team
 * @version 2.0
 */
public class App {
    
    public static void main(String[] args) {
        // Set system properties for better UI rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Initialize the application
        SwingUtilities.invokeLater(() -> {
            try {
                // Set up modern look and feel
                UIManager.setLookAndFeel(new FlatLightLaf());
                
                // Test database connection
                testDatabaseConnection();
                
                // Launch the login screen
                new LoginFrame().setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Failed to initialize application: " + e.getMessage());
                e.printStackTrace();
                
                // Show error dialog and exit
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Failed to start the application. Please check your database connection and try again.\n\nError: " + e.getMessage(),
                    "Application Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
    
    /**
     * Test database connection on startup
     */
    private static void testDatabaseConnection() {
        try {
            DatabaseConnection.getConnection().close();
            System.out.println("✓ Database connection successful");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            System.err.println("\nPlease ensure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'crimedb' exists");
            System.err.println("3. Username and password are correct in DatabaseConnection.java");
            System.err.println("4. Run the database_schema.sql script to create tables");
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
