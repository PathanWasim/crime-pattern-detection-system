package com.crimedetect.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    private static final Properties properties = new Properties();
    private static boolean loaded = false;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        try (InputStream input = ConfigUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                loaded = true;
            } else {
                System.err.println("Warning: application.properties file not found");
            }
        } catch (IOException e) {
            System.err.println("Error loading application.properties: " + e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // Application specific getters
    public static String getAppName() {
        return getProperty("app.name", "Crime Detection System");
    }
    
    public static String getAppVersion() {
        return getProperty("app.version", "1.0");
    }
    
    public static String getDatabaseUrl() {
        return getProperty("db.url", "jdbc:mysql://localhost:3306/crimedb");
    }
    
    public static String getDatabaseUsername() {
        return getProperty("db.username", "root");
    }
    
    public static String getDatabasePassword() {
        return getProperty("db.password", "root");
    }
    
    public static int getWindowWidth() {
        return getIntProperty("ui.window.width", 1280);
    }
    
    public static int getWindowHeight() {
        return getIntProperty("ui.window.height", 720);
    }
    
    public static String getUITheme() {
        return getProperty("ui.theme", "FlatLightLaf");
    }
    
    public static String getFontFamily() {
        return getProperty("ui.font.family", "Segoe UI");
    }
    
    public static int getFontSize() {
        return getIntProperty("ui.font.size", 14);
    }
    
    public static String getExportPath() {
        return getProperty("export.default.path", "./reports/");
    }
    
    public static boolean isFeatureEnabled(String feature) {
        return getBooleanProperty("features." + feature, false);
    }
    
    public static boolean isLoaded() {
        return loaded;
    }
}