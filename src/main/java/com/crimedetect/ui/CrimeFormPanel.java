package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout; // Added for BoxLayout
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CrimeFormPanel extends JPanel {
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final JComboBox<String> typeCombo;
    private final JTextField locationField;
    private final JComboBox<String> statusCombo;
    private final JTextArea descriptionArea;
    
    public CrimeFormPanel() {
        // Using BorderLayout for the overall panel
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JLabel titleLabel = new JLabel("Add New Crime Record");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        add(titleLabel, BorderLayout.NORTH);
        
        // Form Panel using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Crime Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createFormLabel("Crime Type:"), gbc);
        
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"Theft", "Assault", "Burglary", "Fraud"});
        styleFormComponent(typeCombo);
        formPanel.add(typeCombo, gbc);
        
        // Location
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createFormLabel("Location:"), gbc);
        
        gbc.gridx = 1;
        locationField = new JTextField();
        styleFormComponent(locationField);
        formPanel.add(locationField, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createFormLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"Solved", "Unsolved"});
        styleFormComponent(statusCombo);
        formPanel.add(statusCombo, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createFormLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(FIELD_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(0, 100));
        formPanel.add(scrollPane, gbc);
        
        // Buttons Panel using BoxLayout for horizontal arrangement
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        JButton saveButton = createActionButton("Save Record", new Color(76, 175, 80));
        JButton clearButton = createActionButton("Clear", new Color(244, 67, 54));
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonPanel.add(saveButton);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }
    
    private void styleFormComponent(JTextField field) {
        field.setFont(FIELD_FONT);
        field.setPreferredSize(new Dimension(0, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(2, 8, 2, 8)
        ));
    }
    
    private void styleFormComponent(JComboBox<?> combo) {
        combo.setFont(FIELD_FONT);
        combo.setPreferredSize(new Dimension(0, 35));
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        // Action listeners can be attached here
        return button;
    }
}
