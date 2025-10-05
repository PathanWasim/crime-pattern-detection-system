package com.crimedetect.model;

import java.time.LocalDateTime;

public class Admin {
    private int adminId;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Admin(String username, String password, String email) {
        this(username, password);
        this.email = email;
    }

    // Getters
    public int getAdminId() { return adminId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }

    // Setters
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String toString() {
        return String.format("Admin{id=%d, username='%s', email='%s'}", adminId, username, email);
    }
}