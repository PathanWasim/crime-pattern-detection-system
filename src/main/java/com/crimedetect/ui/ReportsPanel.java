package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.crimedetect.db.CrimeDAO;
import com.crimedetect.model.Crime;
import com.crimedetect.utils.ExportUtils;

public class ReportsPanel extends JPanel {
    private final CrimeDAO crimeDAO;
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterCombo;
    private JTextField searchField;
    
    public ReportsPanel() {
        this.crimeDAO = new CrimeDAO();
        initializeUI();
        loadReportData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.CENTER);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Crime Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JLabel infoLabel = new JLabel("Generate and export comprehensive crime reports");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(infoLabel, BorderLayout.SOUTH);
        
        panel.add(textPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Filter Controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlsPanel.setOpaque(false);
        
        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        String[] filterOptions = {"All Crimes", "By Status", "By Type", "By Priority", "By Area", "Recent (Last 30 days)"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton filterBtn = new JButton("Apply Filter");
        styleButton(filterBtn, PRIMARY_COLOR);
        filterBtn.addActionListener(e -> applyFilter());
        
        JButton resetBtn = new JButton("Reset");
        styleButton(resetBtn, new Color(158, 158, 158));
        resetBtn.addActionListener(e -> resetFilter());
        
        controlsPanel.add(filterLabel);
        controlsPanel.add(filterCombo);
        controlsPanel.add(searchLabel);
        controlsPanel.add(searchField);
        controlsPanel.add(filterBtn);
        controlsPanel.add(resetBtn);
        
        // Export Controls
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        exportPanel.setOpaque(false);
        
        JButton exportCsvBtn = new JButton("Export to CSV");
        styleButton(exportCsvBtn, SUCCESS_COLOR);
        exportCsvBtn.addActionListener(e -> exportToCSV());
        
        JButton exportHtmlBtn = new JButton("Export to HTML");
        styleButton(exportHtmlBtn, new Color(255, 152, 0));
        exportHtmlBtn.addActionListener(e -> exportToHTML());
        
        JButton printBtn = new JButton("Print Report");
        styleButton(printBtn, new Color(156, 39, 176));
        printBtn.addActionListener(e -> printReport());
        
        exportPanel.add(exportCsvBtn);
        exportPanel.add(exportHtmlBtn);
        exportPanel.add(printBtn);
        
        panel.add(controlsPanel, BorderLayout.NORTH);
        panel.add(exportPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Statistics Panel
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Type", "Location", "Area", "Date", "Status", "Priority", "Reporter", "Officer"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reportTable = new JTable(tableModel);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportTable.setRowHeight(25);
        reportTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Add double-click listener to show crime details
        reportTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = reportTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int crimeId = (Integer) tableModel.getValueAt(selectedRow, 0);
                        Crime crime = crimeDAO.getCrimeById(crimeId);
                        if (crime != null) {
                            new CrimeDetailsPanel((javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(ReportsPanel.this), crime).setVisible(true);
                        }
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new java.awt.Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Create stat cards
        panel.add(createStatCard("Total Cases", String.valueOf(crimeDAO.getTotalCrimes()), new Color(33, 150, 243)));
        panel.add(createStatCard("Solved", String.valueOf(crimeDAO.getSolvedCases()), new Color(76, 175, 80)));
        panel.add(createStatCard("Pending", String.valueOf(crimeDAO.getPendingCases()), new Color(255, 152, 0)));
        panel.add(createStatCard("High Priority", String.valueOf(getHighPriorityCases()), new Color(244, 67, 54)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
    }
    
    private void loadReportData() {
        tableModel.setRowCount(0);
        List<Crime> crimes = crimeDAO.getAllCrimes();
        
        for (Crime crime : crimes) {
            tableModel.addRow(new Object[]{
                crime.getId(),
                crime.getType(),
                crime.getLocation(),
                crime.getArea(),
                crime.getDateTime().toLocalDate().toString(),
                crime.getStatus(),
                crime.getPriorityLevel(),
                crime.getReporterName(),
                crime.getAssignedOfficer()
            });
        }
    }
    
    private void applyFilter() {
        String filterType = (String) filterCombo.getSelectedItem();
        String searchText = searchField.getText().trim();
        
        tableModel.setRowCount(0);
        List<Crime> crimes;
        
        if (searchText.isEmpty()) {
            crimes = crimeDAO.getAllCrimes();
        } else {
            crimes = crimeDAO.searchCrimesAdvanced(searchText);
        }
        
        // Apply additional filtering based on combo selection
        crimes = crimes.stream()
            .filter(crime -> applyFilterLogic(crime, filterType))
            .toList();
        
        for (Crime crime : crimes) {
            tableModel.addRow(new Object[]{
                crime.getId(),
                crime.getType(),
                crime.getLocation(),
                crime.getArea(),
                crime.getDateTime().toLocalDate().toString(),
                crime.getStatus(),
                crime.getPriorityLevel(),
                crime.getReporterName(),
                crime.getAssignedOfficer()
            });
        }
    }
    
    private boolean applyFilterLogic(Crime crime, String filterType) {
        switch (filterType) {
            case "By Status":
                return !crime.getStatus().equals("Closed");
            case "By Priority":
                return crime.getPriorityLevel().equals("High") || crime.getPriorityLevel().equals("Critical");
            case "Recent (Last 30 days)":
                return crime.getDateTime().isAfter(java.time.LocalDateTime.now().minusDays(30));
            default:
                return true;
        }
    }
    
    private void resetFilter() {
        filterCombo.setSelectedIndex(0);
        searchField.setText("");
        loadReportData();
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setSelectedFile(new File("crime_report.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<Crime> crimes = getCurrentTableData();
            
            if (ExportUtils.exportToCsv(crimes, file.getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Report exported successfully to CSV!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export report!", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToHTML() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
        fileChooser.setSelectedFile(new File("crime_report.html"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<Crime> crimes = getCurrentTableData();
            
            if (ExportUtils.exportToHtml(crimes, file.getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Report exported successfully to HTML!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export report!", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void printReport() {
        try {
            reportTable.print();
        } catch (java.awt.print.PrinterException e) {
            JOptionPane.showMessageDialog(this, "Failed to print report: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<Crime> getCurrentTableData() {
        List<Crime> crimes = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int crimeId = (Integer) tableModel.getValueAt(i, 0);
            Crime crime = crimeDAO.getCrimeById(crimeId);
            if (crime != null) {
                crimes.add(crime);
            }
        }
        return crimes;
    }
    
    private int getHighPriorityCases() {
        return (int) crimeDAO.getAllCrimes().stream()
            .filter(crime -> "High".equals(crime.getPriorityLevel()) || "Critical".equals(crime.getPriorityLevel()))
            .count();
    }
}