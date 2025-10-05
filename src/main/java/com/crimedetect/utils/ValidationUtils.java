package com.crimedetect.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[0-9]{10,15}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("\\s+", "")).matches();
    }
    
    public static boolean isValidDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDateTime.parse(dateTimeStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) return false;
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'&]", "");
    }
    
    public static boolean isValidPriority(String priority) {
        return priority != null && 
               (priority.equals("Low") || priority.equals("Medium") || 
                priority.equals("High") || priority.equals("Critical"));
    }
    
    public static boolean isValidStatus(String status) {
        return status != null && 
               (status.equals("Open") || status.equals("Under Investigation") || 
                status.equals("Pending Evidence") || status.equals("Solved") || 
                status.equals("Closed") || status.equals("Cold Case") || 
                status.equals("Referred") || status.equals("Warrant Issued"));
    }
}