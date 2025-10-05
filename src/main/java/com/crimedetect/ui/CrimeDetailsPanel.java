package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.crimedetect.db.CrimeDAO;
import com.crimedetect.model.Crime;
import com.crimedetect.model.CrimeEvidence;
import com.crimedetect.model.CrimeUpdate;

public class CrimeDetailsPanel extends JDialog {
    private final Crime crime;
    private final CrimeDAO crimeDAO;
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color WARNING_COLOR = new Color(255, 152, 0);
    
    public CrimeDetailsPanel(JFrame parent, Crime crime) {
        super(parent, "Crime Details - ID: " + crime.getId(), true);
        this.crime = crime;
        this.crimeDAO = new CrimeDAO();
        
        initializeUI();
        loadCrimeDetails();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Crime ID: " + crime.getId() + " - " + crime.getType());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel statusLabel = new JLabel("Status: " + crime.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Crime Info Panel
        JPanel infoPanel = createCrimeInfoPanel();
        
        // Evidence and Updates Panel
        JPanel evidenceUpdatesPanel = createEvidenceUpdatesPanel();
        
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(evidenceUpdatesPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCrimeInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Crime Information"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        addInfoRow(panel, gbc, 0, "Type:", crime.getType());
        addInfoRow(panel, gbc, 1, "Location:", crime.getLocation());
        addInfoRow(panel, gbc, 2, "Area:", crime.getArea());
        addInfoRow(panel, gbc, 3, "Date & Time:", crime.getDateTime().format(formatter));
        addInfoRow(panel, gbc, 4, "Priority:", crime.getPriorityLevel());
        addInfoRow(panel, gbc, 5, "Reporter:", crime.getReporterName());
        addInfoRow(panel, gbc, 6, "Assigned Officer:", crime.getAssignedOfficer());
        addInfoRow(panel, gbc, 7, "Evidence Collected:", crime.isEvidenceCollected() ? "Yes" : "No");
        
        // Description
        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        
        JTextArea descArea = new JTextArea(crime.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(new Color(245, 245, 245));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(400, 80));
        panel.add(descScroll, gbc);
        
        return panel;
    }
    
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(labelComp, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(valueComp, gbc);
    }
    
    private JPanel createEvidenceUpdatesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Evidence Panel
        JPanel evidencePanel = new JPanel(new BorderLayout());
        evidencePanel.setBorder(BorderFactory.createTitledBorder("Evidence"));
        
        JTextArea evidenceArea = new JTextArea(8, 30);
        evidenceArea.setEditable(false);
        evidenceArea.setBackground(new Color(250, 250, 250));
        JScrollPane evidenceScroll = new JScrollPane(evidenceArea);
        evidencePanel.add(evidenceScroll, BorderLayout.CENTER);
        
        // Updates Panel
        JPanel updatesPanel = new JPanel(new BorderLayout());
        updatesPanel.setBorder(BorderFactory.createTitledBorder("Case Updates"));
        
        JTextArea updatesArea = new JTextArea(8, 30);
        updatesArea.setEditable(false);
        updatesArea.setBackground(new Color(250, 250, 250));
        JScrollPane updatesScroll = new JScrollPane(updatesArea);
        updatesPanel.add(updatesScroll, BorderLayout.CENTER);
        
        // Load evidence and updates
        loadEvidence(evidenceArea);
        loadUpdates(updatesArea);
        
        panel.add(evidencePanel, BorderLayout.WEST);
        panel.add(updatesPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void loadEvidence(JTextArea evidenceArea) {
        List<CrimeEvidence> evidenceList = crimeDAO.getEvidenceForCrime(crime.getId());
        StringBuilder sb = new StringBuilder();
        
        if (evidenceList.isEmpty()) {
            sb.append("No evidence recorded for this crime.");
        } else {
            for (CrimeEvidence evidence : evidenceList) {
                sb.append("Type: ").append(evidence.getEvidenceType()).append("\n");
                sb.append("Description: ").append(evidence.getDescription()).append("\n");
                sb.append("Collected by: ").append(evidence.getCollectedBy()).append("\n");
                sb.append("Date: ").append(evidence.getCollectedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
                sb.append("---\n");
            }
        }
        
        evidenceArea.setText(sb.toString());
    }
    
    private void loadUpdates(JTextArea updatesArea) {
        List<CrimeUpdate> updatesList = crimeDAO.getUpdatesForCrime(crime.getId());
        StringBuilder sb = new StringBuilder();
        
        if (updatesList.isEmpty()) {
            sb.append("No updates recorded for this crime.");
        } else {
            for (CrimeUpdate update : updatesList) {
                sb.append("Date: ").append(update.getUpdateTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
                sb.append("By: ").append(update.getUpdatedBy()).append("\n");
                sb.append("Update: ").append(update.getUpdateText()).append("\n");
                sb.append("---\n");
            }
        }
        
        updatesArea.setText(sb.toString());
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton addEvidenceBtn = new JButton("Add Evidence");
        addEvidenceBtn.setBackground(SUCCESS_COLOR);
        addEvidenceBtn.setForeground(Color.WHITE);
        addEvidenceBtn.setFocusPainted(false);
        addEvidenceBtn.addActionListener(e -> showAddEvidenceDialog());
        
        JButton addUpdateBtn = new JButton("Add Update");
        addUpdateBtn.setBackground(WARNING_COLOR);
        addUpdateBtn.setForeground(Color.WHITE);
        addUpdateBtn.setFocusPainted(false);
        addUpdateBtn.addActionListener(e -> showAddUpdateDialog());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(158, 158, 158));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        panel.add(addEvidenceBtn);
        panel.add(addUpdateBtn);
        panel.add(closeBtn);
        
        return panel;
    }
    
    private void showAddEvidenceDialog() {
        JDialog dialog = new JDialog(this, "Add Evidence", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField typeField = new JTextField(20);
        JTextArea descArea = new JTextArea(4, 20);
        JTextField collectorField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Evidence Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(typeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JScrollPane(descArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Collected By:"), gbc);
        gbc.gridx = 1;
        dialog.add(collectorField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(e -> {
            if (typeField.getText().trim().isEmpty() || descArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields!");
                return;
            }
            
            CrimeEvidence evidence = new CrimeEvidence(
                crime.getId(),
                typeField.getText().trim(),
                descArea.getText().trim(),
                collectorField.getText().trim()
            );
            
            if (crimeDAO.addEvidence(evidence)) {
                JOptionPane.showMessageDialog(dialog, "Evidence added successfully!");
                dialog.dispose();
                loadCrimeDetails(); // Refresh the display
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add evidence!");
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showAddUpdateDialog() {
        JDialog dialog = new JDialog(this, "Add Case Update", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextArea updateArea = new JTextArea(6, 30);
        JTextField updaterField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Update Text:"), gbc);
        gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH;
        dialog.add(new JScrollPane(updateArea), gbc);
        
        gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(new JLabel("Updated By:"), gbc);
        gbc.gridy = 3;
        dialog.add(updaterField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(e -> {
            if (updateArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter update text!");
                return;
            }
            
            CrimeUpdate update = new CrimeUpdate(
                crime.getId(),
                updateArea.getText().trim(),
                updaterField.getText().trim().isEmpty() ? "System" : updaterField.getText().trim()
            );
            
            if (crimeDAO.addCrimeUpdate(update)) {
                JOptionPane.showMessageDialog(dialog, "Update added successfully!");
                dialog.dispose();
                loadCrimeDetails(); // Refresh the display
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add update!");
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridy = 4;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void loadCrimeDetails() {
        // Refresh the dialog content if needed
        // This method can be called after adding evidence or updates
    }
}