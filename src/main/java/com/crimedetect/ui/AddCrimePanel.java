package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.crimedetect.db.CrimeDAO;
import com.crimedetect.model.Crime;

public class AddCrimePanel extends JPanel {
    // Color scheme
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color ERROR_COLOR = new Color(244, 67, 54);
    
    // Expanded crime type options
    private final String[] crimeTypes = {
        "Theft", "Assault", "Burglary", "Fraud", "Robbery", "Vandalism", 
        "Identity Theft", "Drug Offense", "Cyber Crime", "Arson", 
        "Murder", "Sexual Assault", "Kidnapping", "Hate Crime", "Forgery"
    };
    
    // Expanded status options
    private final String[] statusOptions = {
        "Open", "Under Investigation", "Pending Evidence", "Solved", 
        "Closed", "Cold Case", "Referred", "Warrant Issued"
    };
    
    // Predefined location presets tailored for Pune
    // These are example areas and locations—you can update them as needed.
    private final Map<String, String[]> locationAreas = Map.of(
        "Central", new String[]{"FC Road", "JM Road", "Shivajinagar", "Camp"},
        "North", new String[]{"Viman Nagar", "Koregaon Park", "Wakad"},
        "South", new String[]{"Kalyani Nagar", "Hinjawadi", "Magarpatta"},
        "East", new String[]{"Hadapsar", "Magarpatta", "Pimpri-Chinchwad"},
        "West", new String[]{"Baner", "Aundh", "Pimple Saudagar"}
    );
    
    // UI Components
    private final JComboBox<String> typeCombo = new JComboBox<>(crimeTypes);
    private final JTextField locationField = new JTextField();
    private final JComboBox<String> areaCombo = new JComboBox<>(locationAreas.keySet().toArray(new String[0]));
    private final JComboBox<String> locationCombo = new JComboBox<>();
    private final JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
    private final JTextArea descriptionArea = new JTextArea(5, 20);
    private final JTextField dateField = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    private final JTextField reporterField = new JTextField();
    
    private final CrimeDAO crimeDAO = new CrimeDAO();

    public AddCrimePanel() {
        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JLabel titleLabel = new JLabel("Add New Crime Record");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form Panel using GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Style form components
        styleFormComponent(typeCombo);
        styleFormComponent(locationField);
        styleFormComponent(areaCombo);
        styleFormComponent(locationCombo);
        styleFormComponent(statusCombo);
        styleFormComponent(dateField);
        styleFormComponent(reporterField);
        
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Populate the form using GridBagLayout for better alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Crime Type Section
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        formPanel.add(createFormLabel("Crime Type:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(typeCombo, gbc);
        
        // Location Section
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Area:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(areaCombo, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Location Preset:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(locationCombo, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Custom Location:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(locationField, gbc);
        
        // Additional Info Section
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        addFormSection(formPanel, "Additional Information", gbc);
        gbc.gridwidth = 1;
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Date & Time:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Reported By:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(reporterField, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createFormLabel("Description:"), gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        formPanel.add(scrollPane, gbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton saveButton = createActionButton("Save Record", this::saveCrime, SUCCESS_COLOR);
        JButton clearButton = createActionButton("Clear Form", this::clearForm, ERROR_COLOR);
        
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonPanel.add(saveButton);
        
        // Main layout
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addFormSection(JPanel panel, String title, GridBagConstraints gbc) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(PRIMARY_COLOR);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        panel.add(sectionLabel, gbc);
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }
    
    private void styleFormComponent(JComponent component) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        if (component instanceof JTextField) {
            component.setPreferredSize(new Dimension(0, 35));
            component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
            ));
        } else if (component instanceof JComboBox) {
            component.setPreferredSize(new Dimension(0, 35));
        }
    }

    private JButton createActionButton(String text, Runnable action, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.addActionListener(e -> action.run());
        return button;
    }
    
    private void setupListeners() {
        // Update location presets when area changes
        areaCombo.addActionListener(e -> {
            String selectedArea = (String) areaCombo.getSelectedItem();
            String[] locations = locationAreas.get(selectedArea);
            
            locationCombo.setModel(new DefaultComboBoxModel<>(locations));
        });
        
        // Populate location field when preset is selected
        locationCombo.addActionListener(e -> {
            if (locationCombo.getSelectedItem() != null) {
                locationField.setText((String) locationCombo.getSelectedItem());
            }
        });
        
        // Trigger initial population
        areaCombo.setSelectedIndex(0);
    }

    private void saveCrime() {
        if (!validateInput()) return;
        
        try {
            // Parse date time
            LocalDateTime dateTime = LocalDateTime.parse(
                dateField.getText().trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            
            Crime crime = new Crime(
                (String) typeCombo.getSelectedItem(),
                locationField.getText().trim(),
                dateTime,
                (String) statusCombo.getSelectedItem(),
                descriptionArea.getText().trim()
            );
            
            // Add reporter information if provided
            if (!reporterField.getText().trim().isEmpty()) {
                // Assuming there's a setReporter method or similar
                // crime.setReporter(reporterField.getText().trim());
            }

            if (crimeDAO.addCrime(crime)) {
                showSuccess("Crime record saved successfully!");
                clearForm();
            } else {
                showError("Failed to save crime record!");
            }
        } catch (Exception e) {
            showError("Invalid date format. Please use yyyy-MM-dd HH:mm");
        }
    }

    private boolean validateInput() {
        if (locationField.getText().trim().isEmpty()) {
            showError("Location cannot be empty!");
            return false;
        }
        if (descriptionArea.getText().trim().isEmpty()) {
            showError("Description cannot be empty!");
            return false;
        }
        if (dateField.getText().trim().isEmpty()) {
            showError("Date cannot be empty!");
            return false;
        }
        
        // Validate date format
        try {
            LocalDateTime.parse(
                dateField.getText().trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
        } catch (Exception e) {
            showError("Invalid date format. Please use yyyy-MM-dd HH:mm");
            return false;
        }
        
        return true;
    }

    private void clearForm() {
        typeCombo.setSelectedIndex(0);
        areaCombo.setSelectedIndex(0);
        locationCombo.setSelectedIndex(0);
        locationField.setText("");
        statusCombo.setSelectedIndex(0);
        dateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        reporterField.setText("");
        descriptionArea.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
