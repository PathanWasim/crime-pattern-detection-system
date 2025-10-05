package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.crimedetect.db.CrimeDAO;
import com.crimedetect.model.Crime;

public class SearchCrimesPanel extends JPanel {
    private final JTable crimeTable;
    private final DefaultTableModel tableModel;
    private final CrimeDAO crimeDAO;
    
    public SearchCrimesPanel() {
        crimeDAO = new CrimeDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header title
        JLabel titleLabel = new JLabel("Search Crime Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        add(titleLabel, BorderLayout.NORTH);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Search by Location:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.addActionListener(e -> searchCrimes(searchField.getText()));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        add(searchPanel, BorderLayout.SOUTH);
        
        // Table for crime records
        String[] columns = {"ID", "Type", "Location", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        crimeTable = new JTable(tableModel);
        crimeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        crimeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        crimeTable.setRowHeight(30);
        
        add(new JScrollPane(crimeTable), BorderLayout.CENTER);
        
        // Load initial data
        loadCrimes();
    }
    
    private void loadCrimes() {
        tableModel.setRowCount(0);
        List<Crime> crimes = crimeDAO.getAllCrimes();
        for (Crime crime : crimes) {
            tableModel.addRow(new Object[]{
                crime.getId(),
                crime.getType(),
                crime.getLocation(),
                crime.getDateTime().toLocalDate().toString(),
                crime.getStatus()
            });
        }
    }
    
    private void searchCrimes(String keyword) {
        tableModel.setRowCount(0);
        List<Crime> filtered = crimeDAO.getAllCrimes().stream()
            .filter(c -> c.getLocation().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
        
        filtered.forEach(c -> tableModel.addRow(new Object[]{
            c.getId(),
            c.getType(),
            c.getLocation(),
            c.getDateTime().toLocalDate().toString(),
            c.getStatus()
        }));
    }
}
