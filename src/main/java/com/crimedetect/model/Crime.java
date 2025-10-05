package com.crimedetect.model;

import java.time.LocalDateTime;

public class Crime {
    private int id;
    private String type;
    private String location;
    private String area;
    private LocalDateTime dateTime;
    private String status;
    private String description;
    private String reporterName;
    private String reporterContact;
    private String priorityLevel;
    private String assignedOfficer;
    private boolean evidenceCollected;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Main constructor
    public Crime(String type, String location, LocalDateTime dateTime, String status, String description) {
        this.type = type;
        this.location = location;
        this.dateTime = dateTime;
        this.status = status;
        this.description = description;
        this.priorityLevel = "Medium"; // Default priority
        this.evidenceCollected = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Enhanced constructor
    public Crime(String type, String location, String area, LocalDateTime dateTime, String status, 
                 String description, String reporterName, String priorityLevel) {
        this(type, location, dateTime, status, description);
        this.area = area;
        this.reporterName = reporterName;
        this.priorityLevel = priorityLevel;
    }

    // Getters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public String getArea() { return area; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getReporterName() { return reporterName; }
    public String getReporterContact() { return reporterContact; }
    public String getPriorityLevel() { return priorityLevel; }
    public String getAssignedOfficer() { return assignedOfficer; }
    public boolean isEvidenceCollected() { return evidenceCollected; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setLocation(String location) { this.location = location; }
    public void setArea(String area) { this.area = area; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public void setReporterContact(String reporterContact) { this.reporterContact = reporterContact; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }
    public void setAssignedOfficer(String assignedOfficer) { this.assignedOfficer = assignedOfficer; }
    public void setEvidenceCollected(boolean evidenceCollected) { this.evidenceCollected = evidenceCollected; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Crime{id=%d, type='%s', location='%s', status='%s', priority='%s'}", 
                           id, type, location, status, priorityLevel);
    }
}