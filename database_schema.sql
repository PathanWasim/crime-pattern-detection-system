-- Crime Detection System Database Schema
-- Run this script to create the required database and tables

CREATE DATABASE IF NOT EXISTS crimedb;
USE crimedb;

-- Admin table for authentication
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Crime table with enhanced fields
CREATE TABLE IF NOT EXISTS crime (
    crime_id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    area VARCHAR(100),
    date_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT,
    reporter_name VARCHAR(100),
    reporter_contact VARCHAR(20),
    priority_level ENUM('Low', 'Medium', 'High', 'Critical') DEFAULT 'Medium',
    assigned_officer VARCHAR(100),
    evidence_collected BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crime evidence table
CREATE TABLE IF NOT EXISTS crime_evidence (
    evidence_id INT PRIMARY KEY AUTO_INCREMENT,
    crime_id INT,
    evidence_type VARCHAR(100),
    description TEXT,
    file_path VARCHAR(500),
    collected_by VARCHAR(100),
    collected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (crime_id) REFERENCES crime(crime_id) ON DELETE CASCADE
);

-- Crime updates/notes table
CREATE TABLE IF NOT EXISTS crime_updates (
    update_id INT PRIMARY KEY AUTO_INCREMENT,
    crime_id INT,
    update_text TEXT NOT NULL,
    updated_by VARCHAR(100),
    update_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (crime_id) REFERENCES crime(crime_id) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123)
INSERT INTO admin (username, password, email) VALUES 
('admin', 'admin123', 'admin@crimedetect.com')
ON DUPLICATE KEY UPDATE username = username;

-- Insert sample crime data
INSERT INTO crime (type, location, area, date_time, status, description, reporter_name, priority_level) VALUES
('Theft', 'FC Road', 'Central', '2024-01-15 14:30:00', 'Under Investigation', 'Motorcycle theft reported near FC Road', 'John Doe', 'High'),
('Assault', 'Koregaon Park', 'North', '2024-01-20 22:15:00', 'Solved', 'Physical assault case resolved', 'Jane Smith', 'Medium'),
('Burglary', 'Hinjawadi', 'West', '2024-02-05 03:45:00', 'Open', 'House break-in reported', 'Mike Johnson', 'High'),
('Fraud', 'Viman Nagar', 'North', '2024-02-10 10:20:00', 'Pending Evidence', 'Online fraud case', 'Sarah Wilson', 'Medium'),
('Vandalism', 'Camp Area', 'Central', '2024-02-15 16:00:00', 'Closed', 'Property damage case closed', 'Tom Brown', 'Low'),
('Cyber Crime', 'Magarpatta', 'South', '2024-03-01 11:30:00', 'Under Investigation', 'Data breach investigation ongoing', 'Lisa Davis', 'Critical'),
('Drug Offense', 'Wakad', 'North', '2024-03-05 19:45:00', 'Solved', 'Drug possession case resolved', 'Robert Miller', 'High'),
('Identity Theft', 'Baner', 'West', '2024-03-10 09:15:00', 'Open', 'Identity theft reported', 'Emily Garcia', 'Medium');

-- Create indexes for better performance
CREATE INDEX idx_crime_type ON crime(type);
CREATE INDEX idx_crime_location ON crime(location);
CREATE INDEX idx_crime_status ON crime(status);
CREATE INDEX idx_crime_date ON crime(date_time);
CREATE INDEX idx_crime_area ON crime(area);