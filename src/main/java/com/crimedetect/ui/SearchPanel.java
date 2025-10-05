package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SearchPanel extends JPanel {
    private JTable crimeTable;
    
    public SearchPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Crime Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"All", "Theft", "Assault", "Burglary", "Fraud"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField locationField = new JTextField(15);
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Add action listener as needed
        
        filterPanel.add(typeLabel);
        filterPanel.add(typeCombo);
        filterPanel.add(locationLabel);
        filterPanel.add(locationField);
        filterPanel.add(searchButton);
        
        // Table Panel
        String[] columns = {"ID", "Type", "Location", "Date", "Status"};
        Object[][] data = {}; // Data to be loaded dynamically
        crimeTable = new JTable(data, columns);
        crimeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        crimeTable.setFillsViewportHeight(true);
        
        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(crimeTable), BorderLayout.CENTER);
    }
}
