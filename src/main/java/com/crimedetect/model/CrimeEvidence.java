package com.crimedetect.model;

import java.time.LocalDateTime;

public class CrimeEvidence {
    private int evidenceId;
    private int crimeId;
    private String evidenceType;
    private String description;
    private String filePath;
    private String collectedBy;
    private LocalDateTime collectedAt;

    public CrimeEvidence(int crimeId, String evidenceType, String description, String collectedBy) {
        this.crimeId = crimeId;
        this.evidenceType = evidenceType;
        this.description = description;
        this.collectedBy = collectedBy;
        this.collectedAt = LocalDateTime.now();
    }

    // Getters
    public int getEvidenceId() { return evidenceId; }
    public int getCrimeId() { return crimeId; }
    public String getEvidenceType() { return evidenceType; }
    public String getDescription() { return description; }
    public String getFilePath() { return filePath; }
    public String getCollectedBy() { return collectedBy; }
    public LocalDateTime getCollectedAt() { return collectedAt; }

    // Setters
    public void setEvidenceId(int evidenceId) { this.evidenceId = evidenceId; }
    public void setCrimeId(int crimeId) { this.crimeId = crimeId; }
    public void setEvidenceType(String evidenceType) { this.evidenceType = evidenceType; }
    public void setDescription(String description) { this.description = description; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setCollectedBy(String collectedBy) { this.collectedBy = collectedBy; }
    public void setCollectedAt(LocalDateTime collectedAt) { this.collectedAt = collectedAt; }
}