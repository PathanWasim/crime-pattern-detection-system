package com.crimedetect.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.crimedetect.ui.DashboardFrame;
import com.crimedetect.utils.DatabaseConnection;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        FlatLightLaf.setup();
        setTitle("Crime Detection System - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel with Gradient Background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(40, 50, 75); // Dark Blue
                Color color2 = new Color(75, 100, 150); // Light Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png")); // Add your logo
        logoLabel.setPreferredSize(new Dimension(100, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(logoLabel, gbc);

        // Username Field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        styleTextField(usernameField);
        mainPanel.add(usernameField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        styleTextField(passwordField);
        mainPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(76, 175, 80)); // Green
        loginButton.addActionListener(e -> authenticate());
        mainPanel.add(loginButton, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

   private void authenticate() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    try (Connection conn = DatabaseConnection.getConnection()) {
        String query = "SELECT password FROM admin WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedPassword = rs.getString("password");
            if (password.equals(storedPassword)) {
                // Close login window and open dashboard
                SwingUtilities.invokeLater(() -> {
                    this.dispose(); // Close login window
                    new DashboardFrame().setVisible(true); // Open dashboard
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "User Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public static void main(String[] args) {
        new LoginFrame();
    }
}