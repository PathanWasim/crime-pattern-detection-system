package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SettingsPanel extends JPanel {
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    public SettingsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Account Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        add(titleLabel, BorderLayout.NORTH);
        
        // Form Panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username (read-only, if required)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createFormLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        JTextField usernameField = new JTextField("admin");
        usernameField.setFont(FIELD_FONT);
        usernameField.setEditable(false);
        usernameField.setPreferredSize(new Dimension(0, 35));
        formPanel.add(usernameField, gbc);
        
        // New Password
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createFormLabel("New Password:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField newPassField = new JPasswordField();
        styleFormComponent(newPassField);
        formPanel.add(newPassField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createFormLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField confirmPassField = new JPasswordField();
        styleFormComponent(confirmPassField);
        formPanel.add(confirmPassField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton saveButton = createActionButton("Save Changes", new Color(33, 150, 243));
        buttonPanel.add(saveButton);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }
    
    private void styleFormComponent(JPasswordField field) {
        field.setFont(FIELD_FONT);
        field.setPreferredSize(new Dimension(0, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(2, 8, 2, 8)
        ));
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        // Action listener for saving changes can be attached here
        return button;
    }
}
