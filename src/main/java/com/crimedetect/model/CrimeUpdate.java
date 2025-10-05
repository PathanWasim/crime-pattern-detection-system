package com.crimedetect.model;

import java.time.LocalDateTime;

public class CrimeUpdate {
    private int updateId;
    private int crimeId;
    private String updateText;
    private String updatedBy;
    private LocalDateTime updateTimestamp;

    public CrimeUpdate(int crimeId, String updateText, String updatedBy) {
        this.crimeId = crimeId;
        this.updateText = updateText;
        this.updatedBy = updatedBy;
        this.updateTimestamp = LocalDateTime.now();
    }

    // Getters
    public int getUpdateId() { return updateId; }
    public int getCrimeId() { return crimeId; }
    public String getUpdateText() { return updateText; }
    public String getUpdatedBy() { return updatedBy; }
    public LocalDateTime getUpdateTimestamp() { return updateTimestamp; }

    // Setters
    public void setUpdateId(int updateId) { this.updateId = updateId; }
    public void setCrimeId(int crimeId) { this.crimeId = crimeId; }
    public void setUpdateText(String updateText) { this.updateText = updateText; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public void setUpdateTimestamp(LocalDateTime updateTimestamp) { this.updateTimestamp = updateTimestamp; }
}