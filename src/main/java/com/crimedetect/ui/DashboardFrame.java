package com.crimedetect.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.crimedetect.db.CrimeDAO;
import com.crimedetect.model.Crime;
import com.crimedetect.utils.DatabaseConnection;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class DashboardFrame extends JFrame {
    private JPanel mainContent;
    private CardLayout cardLayout;
    private final CrimeDAO crimeDAO = new CrimeDAO();
    private Timer refreshTimer;
    private JTable crimeTable;
    private JButton activeButton; // Track currently active button

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color ACCENT_COLOR = new Color(66, 165, 245);
    private final Color SIDEBAR_BG = new Color(33, 33, 33);
    private final Color SIDEBAR_BUTTON = new Color(50, 50, 50);
    private final Color SIDEBAR_ACTIVE = new Color(25, 118, 210);
    private final Color CARD_1 = new Color(26, 35, 126);
    private final Color CARD_2 = new Color(0, 105, 92);
    private final Color CARD_3 = new Color(183, 28, 28);
    private final Color CARD_4 = new Color(130, 119, 23);

    public DashboardFrame() {
        setupLookAndFeel();
        setTitle("Crime Detection System - Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createMainContent(), BorderLayout.CENTER);

        add(mainPanel);
        startAutoRefresh(5);
    }
    
    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            // Customize UI components
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextField.caretBlinkRate", 500);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.rowHeight", 30);
            UIManager.put("Table.cellMargins", new Dimension(8, 4));
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // App Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(SIDEBAR_BG);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        
        JLabel appTitle = new JLabel("Crime Detection");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel systemLabel = new JLabel("System");
        systemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        systemLabel.setForeground(new Color(200, 200, 200));
        systemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(appTitle);
        titlePanel.add(systemLabel);
        sidebar.add(titlePanel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Menu Items with Icons
        String[] menuItems = {"Dashboard", "Add Crime", "Search Crimes", "Analytics", "Settings"};
        String[] icons = {"dashboard.png", "add.png", "search.png", "analytics.png", "settings.png"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = createSidebarButton(menuItems[i], icons[i]);
            if (i == 0) { // Activate Dashboard by default
                activeButton = btn;
                btn.setBackground(SIDEBAR_ACTIVE);
            }
            btn.addActionListener(new MenuButtonListener());
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // Logout button at bottom
        JButton logoutBtn = createSidebarButton("Logout", "logout.png");
        logoutBtn.setBackground(new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> handleLogout());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }
    
    private JButton createSidebarButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setHorizontalAlignment(JButton.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setMaximumSize(new Dimension(250, 50));
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + iconPath));
            btn.setIcon(icon);
        } catch (Exception e) {
            // If icon not found, just use text
        }
        
        return btn;
    }

    private JPanel createMainContent() {
        mainContent = new JPanel();
        cardLayout = new CardLayout();
        mainContent.setLayout(cardLayout);
    
        // Ensure panel names match exactly with button commands
        mainContent.add(createDashboardPanel(), "dashboard");
        mainContent.add(new AddCrimePanel(), "addcrime");
        mainContent.add(createSearchPanel(), "searchcrimes");
        mainContent.add(createAnalyticsPanel(), "analytics");
        mainContent.add(createSettingsPanel(), "settings");
    
        return mainContent;
    }

    // ------------------- Dashboard Panel -------------------
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with welcome message and date
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel welcomeLabel = new JLabel("Crime Statistics Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(50, 50, 50));
        
        JLabel dateLabel = new JLabel("Today: " + java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        // Real-time Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setOpaque(false);
        
        statsPanel.add(createStatCard("Total Crimes", String.valueOf(crimeDAO.getTotalCrimes()), CARD_1));
        statsPanel.add(createStatCard("Solved Cases", String.valueOf(crimeDAO.getSolvedCases()), CARD_2));
        statsPanel.add(createStatCard("Pending Cases", String.valueOf(crimeDAO.getPendingCases()), CARD_3));
        statsPanel.add(createStatCard("Top Location", crimeDAO.getTopLocation(), CARD_4));

        // Recent Activity Panel
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        ));
        
        JLabel recentLabel = new JLabel("Recent Crimes");
        recentLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Table for recent crimes
        String[] columns = {"ID", "Type", "Location", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable recentTable = new JTable(model);
        recentTable.setRowHeight(30);
        recentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        recentTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Retrieve all crimes and use the first 5 as recent crimes
        List<Crime> allCrimes = crimeDAO.getAllCrimes();
        int count = Math.min(allCrimes.size(), 5);
        List<Crime> recentCrimes = allCrimes.subList(0, count);
        for (Crime crime : recentCrimes) {
            model.addRow(new Object[]{
                crime.getId(),
                crime.getType(),
                crime.getLocation(),
                crime.getDateTime().toLocalDate().toString(),
                crime.getStatus()
            });
        }
        
        recentPanel.add(recentLabel, BorderLayout.NORTH);
        recentPanel.add(new JScrollPane(recentTable), BorderLayout.CENTER);

        // Charts Panel - 2 charts side by side
        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartPanel.setOpaque(false);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        chartPanel.add(createStyledMonthlyTrendsChart());
        chartPanel.add(createStyledCrimeTypeChart());
        
        // Assemble Dashboard Components
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(recentPanel, BorderLayout.CENTER);
        contentPanel.add(chartPanel, BorderLayout.SOUTH);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }
    
    /**
     * Helper method to create a stat card.
     */
    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ------------------- Search Panel -------------------
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Page Title
        JLabel titleLabel = new JLabel("Crime Records Search");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    
        // Search Components
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel searchLabel = new JLabel("Search Criteria");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        inputPanel.setOpaque(false);
        
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 35));
        
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(240, 240, 240));
        resetButton.setForeground(new Color(80, 80, 80));
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        searchButton.addActionListener(e -> refreshSearchResults(searchField.getText().trim()));
        resetButton.addActionListener(e -> {
            searchField.setText("");
            refreshSearchResults("");
        });
    
        inputPanel.add(new JLabel("Search by Location:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(resetButton);
        
        searchPanel.add(searchLabel);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        searchPanel.add(inputPanel);
    
        // Table Initialization
        String[] columns = {"ID", "Type", "Location", "Date", "Status", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        this.crimeTable = new JTable(model);
        crimeTable.setRowHeight(40);
        crimeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        crimeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        crimeTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        refreshSearchResults("");
        
        JScrollPane tableScrollPane = new JScrollPane(crimeTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);
    
        return panel;
    }

    // ------------------- Analytics Panel -------------------
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title and descriptive text
        JLabel titleLabel = new JLabel("Crime Analytics Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JLabel infoLabel = new JLabel("<html><body style='width: 500px;'>"
            + "This dashboard provides insights into crime trends in Pune. "
            + "It shows monthly trends, the distribution of crimes by location, "
            + "crime statuses, and crime types. Use these insights to identify "
            + "areas with high activity and track progress in solving cases."
            + "</body></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(80, 80, 80));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(infoLabel, BorderLayout.SOUTH);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Charts Panel
        JPanel chartsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        chartsPanel.setOpaque(false);
        
        // Populate the datasets. Fallback sample data is provided if datasets are empty.
        DefaultCategoryDataset monthlyData = crimeDAO.getYearlyTrends();
        if (monthlyData.getRowCount() == 0) {
            monthlyData.addValue(15, "Crimes", "Jan");
            monthlyData.addValue(18, "Crimes", "Feb");
            monthlyData.addValue(23, "Crimes", "Mar");
            monthlyData.addValue(15, "Crimes", "Apr");
        }
        
        DefaultCategoryDataset locationData = crimeDAO.getLocationAnalysis();
        if (locationData.getRowCount() == 0) {
            locationData.addValue(35, "Crimes", "Central");
            locationData.addValue(22, "Crimes", "North");
            locationData.addValue(18, "Crimes", "South");
            locationData.addValue(15, "Crimes", "West");
        }
        
        DefaultPieDataset<String> statusData = crimeDAO.getStatusDistribution();
        if (statusData.getItemCount() == 0) {
            statusData.setValue("Solved", 60);
            statusData.setValue("Unsolved", 40);
        }
        
        DefaultCategoryDataset typeData = crimeDAO.getTypeTrends();
        if (typeData.getRowCount() == 0) {
            typeData.addValue(45, "Crimes", "Theft");
            typeData.addValue(28, "Crimes", "Assault");
            typeData.addValue(15, "Crimes", "Burglary");
            typeData.addValue(12, "Crimes", "Fraud");
        }
        
        chartsPanel.add(createStyledLineChart("Monthly Crime Trends", monthlyData));
        chartsPanel.add(createStyledBarChart("Crimes by Location", locationData));
        chartsPanel.add(createStyledPieChart("Crime Status Distribution", statusData));
        chartsPanel.add(createStyledBarChart("Crime Types", typeData));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(chartsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    

    // ------------------- Settings Panel -------------------
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Account Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            )
        ));
        
        JPanel currentPassPanel = createFormField("Current Password", new JPasswordField());
        JPanel newPassPanel = createFormField("New Password", new JPasswordField());
        JPanel confirmPassPanel = createFormField("Confirm Password", new JPasswordField());
        
        JPasswordField currentPassField = (JPasswordField) ((JPanel) currentPassPanel.getComponent(1)).getComponent(0);
        JPasswordField newPassField = (JPasswordField) ((JPanel) newPassPanel.getComponent(1)).getComponent(0);
        JPasswordField confirmPassField = (JPasswordField) ((JPanel) confirmPassPanel.getComponent(1)).getComponent(0);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        saveButton.addActionListener(e -> handlePasswordChange(
            new String(currentPassField.getPassword()),
            new String(newPassField.getPassword()),
            new String(confirmPassField.getPassword())
        ));
        
        buttonPanel.add(saveButton);
        
        formPanel.add(currentPassPanel);
        formPanel.add(newPassPanel);
        formPanel.add(confirmPassPanel);
        formPanel.add(buttonPanel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormField(String labelText, Component field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 8));
        fieldPanel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            ((JTextField) field).setPreferredSize(new Dimension(0, 35));
        }
        
        inputPanel.add(field, BorderLayout.CENTER);
        
        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(inputPanel, BorderLayout.CENTER);
        
        return fieldPanel;
    }

    // ------------------- Chart Creation Methods -------------------
    private ChartPanel createStyledMonthlyTrendsChart() {
        DefaultCategoryDataset dataset = crimeDAO.getMonthlyTrends();
        
        if (dataset.getRowCount() == 0) {
            dataset.addValue(15, "Crimes", "Jan");
            dataset.addValue(18, "Crimes", "Feb");
            dataset.addValue(23, "Crimes", "Mar");
            dataset.addValue(12, "Crimes", "Apr");
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Monthly Crime Trends", 
            "Month", 
            "Cases", 
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        // Adjust bar spacing and width
        renderer.setItemMargin(0.02);
        renderer.setMaximumBarWidth(0.1);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, new Color(25, 118, 210));
        
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(230, 230, 230));
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        
        ChartPanel chartPanel = new ChartPanel(chart);
        // Set a preferred size so the chart is not overly tall
        chartPanel.setPreferredSize(new Dimension(500, 350));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return chartPanel;
    }

    private ChartPanel createStyledCrimeTypeChart() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        crimeDAO.getCrimeTypeDistribution().forEach((type, count) -> 
            dataset.setValue(type, count)
        );
        
        if (dataset.getItemCount() == 0) {
            dataset.setValue("Theft", 45);
            dataset.setValue("Assault", 25);
            dataset.setValue("Burglary", 15);
            dataset.setValue("Fraud", 15);
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Crime Type Distribution", 
            dataset, 
            true,
            true,
            false
        );
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Theft", new Color(25, 118, 210));
        plot.setSectionPaint("Assault", new Color(244, 67, 54));
        plot.setSectionPaint("Burglary", new Color(255, 152, 0));
        plot.setSectionPaint("Fraud", new Color(76, 175, 80));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        chart.setBackgroundPaint(Color.WHITE);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 350));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return chartPanel;
    }

    private ChartPanel createStyledLineChart(String title, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
            title, 
            "Period", 
            "Number of Cases", 
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(25, 118, 210));
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, true);
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(230, 230, 230));
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 350));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return chartPanel;
    }

    private ChartPanel createStyledBarChart(String title, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
            title, 
            "Category", 
            "Number of Cases", 
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 150, 136));
        // Adjust bar renderer settings for consistency
        renderer.setItemMargin(0.02);
        renderer.setMaximumBarWidth(0.1);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(230, 230, 230));
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 350));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return chartPanel;
    }

    private ChartPanel createStyledPieChart(String title, DefaultPieDataset<String> dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
            title, 
            dataset, 
            true,
            true,
            false
        );
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Solved", new Color(76, 175, 80));
        plot.setSectionPaint("Unsolved", new Color(244, 67, 54));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        chart.setBackgroundPaint(Color.WHITE);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 350));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return chartPanel;
    }

    private void handlePasswordChange(String currentPass, String newPass, String confirmPass) {
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showStyledMessage("All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPass.equals(confirmPass)) {
            showStyledMessage("New passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPass.length() < 6) {
            showStyledMessage("Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String verifyQuery = "SELECT COUNT(*) FROM admin WHERE username = 'admin' AND password = ?";
            PreparedStatement verifyStmt = conn.prepareStatement(verifyQuery);
            verifyStmt.setString(1, currentPass);
            
            if (verifyStmt.executeQuery().getInt(1) == 0) {
                showStyledMessage("Current password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String updateQuery = "UPDATE admin SET password = ? WHERE username = 'admin'";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, newPass);
            updateStmt.executeUpdate();
            
            showStyledMessage("Password updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showStyledMessage("Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshSearchResults(String keyword) {
        DefaultTableModel model = (DefaultTableModel) crimeTable.getModel();
        model.setRowCount(0);
        
        List<Crime> filteredCrimes = crimeDAO.searchCrimes(keyword);
        for (Crime crime : filteredCrimes) {
            model.addRow(new Object[]{
                crime.getId(),
                crime.getType(),
                crime.getLocation(),
                crime.getDateTime().toLocalDate().toString(),
                crime.getStatus(),
                "View Details"
            });
        }
        
        if (filteredCrimes.isEmpty() && !keyword.isEmpty()) {
            showStyledMessage("No matching records found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ------------------- Auto-Refresh Methods -------------------
    private void startAutoRefresh(int seconds) {
        refreshTimer = new Timer(seconds * 1000, e -> {
            if (cardLayout.toString().contains("dashboard")) {
                refreshDashboardData();
            }
        });
        refreshTimer.setRepeats(true);
        refreshTimer.start();
    }

    private void refreshDashboardData() {
        try {
            mainContent.remove(0);
            mainContent.add(createDashboardPanel(), "dashboard", 0);
            if (cardLayout.toString().contains("dashboard")) {
                cardLayout.show(mainContent, "dashboard");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // Launch login window if needed
        }
    }

    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane optionPane = new JOptionPane(
            message,
            messageType
        );
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dialog.setVisible(true);
    }

    private class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (activeButton != null) {
                activeButton.setBackground(SIDEBAR_BUTTON);
            }
            clicked.setBackground(SIDEBAR_ACTIVE);
            activeButton = clicked;
            
            String command = clicked.getText().toLowerCase().replace(" ", "");
            cardLayout.show(mainContent, command);
        }
    }

    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
