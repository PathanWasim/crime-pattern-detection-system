package com.crimedetect.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.crimedetect.model.Crime;

public class ExportUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static boolean exportToCsv(List<Crime> crimes, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write CSV header
            writer.append("ID,Type,Location,Area,Date Time,Status,Description,Reporter,Priority,Officer,Evidence Collected\n");
            
            // Write crime data
            for (Crime crime : crimes) {
                writer.append(String.valueOf(crime.getId())).append(",");
                writer.append(escapeCSV(crime.getType())).append(",");
                writer.append(escapeCSV(crime.getLocation())).append(",");
                writer.append(escapeCSV(crime.getArea())).append(",");
                writer.append(crime.getDateTime().format(DATE_FORMATTER)).append(",");
                writer.append(escapeCSV(crime.getStatus())).append(",");
                writer.append(escapeCSV(crime.getDescription())).append(",");
                writer.append(escapeCSV(crime.getReporterName())).append(",");
                writer.append(escapeCSV(crime.getPriorityLevel())).append(",");
                writer.append(escapeCSV(crime.getAssignedOfficer())).append(",");
                writer.append(String.valueOf(crime.isEvidenceCollected())).append("\n");
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean exportToHtml(List<Crime> crimes, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html><head><title>Crime Report</title>\n");
            writer.write("<style>\n");
            writer.write("table { border-collapse: collapse; width: 100%; }\n");
            writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("th { background-color: #f2f2f2; }\n");
            writer.write("tr:nth-child(even) { background-color: #f9f9f9; }\n");
            writer.write("</style></head><body>\n");
            writer.write("<h1>Crime Detection System Report</h1>\n");
            writer.write("<p>Generated on: " + java.time.LocalDateTime.now().format(DATE_FORMATTER) + "</p>\n");
            writer.write("<table>\n");
            writer.write("<tr><th>ID</th><th>Type</th><th>Location</th><th>Area</th><th>Date</th><th>Status</th><th>Priority</th></tr>\n");
            
            for (Crime crime : crimes) {
                writer.write("<tr>");
                writer.write("<td>" + crime.getId() + "</td>");
                writer.write("<td>" + escapeHtml(crime.getType()) + "</td>");
                writer.write("<td>" + escapeHtml(crime.getLocation()) + "</td>");
                writer.write("<td>" + escapeHtml(crime.getArea()) + "</td>");
                writer.write("<td>" + crime.getDateTime().format(DATE_FORMATTER) + "</td>");
                writer.write("<td>" + escapeHtml(crime.getStatus()) + "</td>");
                writer.write("<td>" + escapeHtml(crime.getPriorityLevel()) + "</td>");
                writer.write("</tr>\n");
            }
            
            writer.write("</table>\n");
            writer.write("</body></html>");
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private static String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
}