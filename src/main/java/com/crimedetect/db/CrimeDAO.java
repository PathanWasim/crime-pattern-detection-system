package com.crimedetect.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.crimedetect.model.Crime;
import com.crimedetect.model.CrimeEvidence;
import com.crimedetect.model.CrimeUpdate;
import com.crimedetect.utils.DatabaseConnection;
import com.crimedetect.utils.DateUtils;

public class CrimeDAO {
    // Enhanced SQL queries
    private static final String INSERT_CRIME = """
        INSERT INTO crime (type, location, area, date_time, status, description, 
                          reporter_name, reporter_contact, priority_level, assigned_officer) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
    
    private static final String SELECT_ALL = """
        SELECT crime_id, type, location, area, date_time, status, description, 
               reporter_name, reporter_contact, priority_level, assigned_officer, 
               evidence_collected, created_at, updated_at 
        FROM crime ORDER BY created_at DESC
        """;
    
    private static final String UPDATE_CRIME = """
        UPDATE crime SET type=?, location=?, area=?, status=?, description=?, 
                        priority_level=?, assigned_officer=?, updated_at=CURRENT_TIMESTAMP 
        WHERE crime_id=?
        """;
    
    private static final String DELETE_CRIME = "DELETE FROM crime WHERE crime_id=?";
    
    private static final String SEARCH_CRIMES = """
        SELECT * FROM crime 
        WHERE (location LIKE ? OR type LIKE ? OR status LIKE ? OR area LIKE ?) 
        ORDER BY created_at DESC
        """;
    public List<Crime> searchCrimes(String keyword) {
        List<Crime> crimes = new ArrayList<>();
        String query = "SELECT * FROM crime WHERE location LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Crime crime = new Crime(
                    rs.getString("type"),
                    rs.getString("location"),
                    rs.getTimestamp("date_time").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getString("description")
                );
                crime.setId(rs.getInt("crime_id"));
                crimes.add(crime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crimes;
    }

    public int getTotalCrimes() {
        String query = "SELECT COUNT(*) FROM crime";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getSolvedCases() {
        return getCountByStatus("Solved");
    }

    public int getPendingCases() {
        return getCountByStatus("Unsolved");
    }

    public String getTopLocation() {
        String query = "SELECT location, COUNT(*) as count FROM crime GROUP BY location ORDER BY count DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getString("location") : "N/A";
        } catch (SQLException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    private int getCountByStatus(String status) {
        String query = "SELECT COUNT(*) FROM crime WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public Map<String, Integer> getCrimeTypeDistribution() {
    Map<String, Integer> distribution = new HashMap<>();
    String query = "SELECT type, COUNT(*) as count FROM crime GROUP BY type";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            distribution.put(rs.getString("type"), rs.getInt("count"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return distribution;
}

    // Evidence management methods
    public boolean addEvidence(CrimeEvidence evidence) {
        String sql = "INSERT INTO crime_evidence (crime_id, evidence_type, description, file_path, collected_by) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, evidence.getCrimeId());
            stmt.setString(2, evidence.getEvidenceType());
            stmt.setString(3, evidence.getDescription());
            stmt.setString(4, evidence.getFilePath());
            stmt.setString(5, evidence.getCollectedBy());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CrimeEvidence> getEvidenceForCrime(int crimeId) {
        List<CrimeEvidence> evidenceList = new ArrayList<>();
        String query = "SELECT * FROM crime_evidence WHERE crime_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, crimeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CrimeEvidence evidence = new CrimeEvidence(
                    rs.getInt("crime_id"),
                    rs.getString("evidence_type"),
                    rs.getString("description"),
                    rs.getString("collected_by")
                );
                evidence.setEvidenceId(rs.getInt("evidence_id"));
                evidence.setFilePath(rs.getString("file_path"));
                evidence.setCollectedAt(rs.getTimestamp("collected_at").toLocalDateTime());
                evidenceList.add(evidence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evidenceList;
    }

    // Crime updates management
    public boolean addCrimeUpdate(CrimeUpdate update) {
        String sql = "INSERT INTO crime_updates (crime_id, update_text, updated_by) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, update.getCrimeId());
            stmt.setString(2, update.getUpdateText());
            stmt.setString(3, update.getUpdatedBy());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CrimeUpdate> getUpdatesForCrime(int crimeId) {
        List<CrimeUpdate> updates = new ArrayList<>();
        String query = "SELECT * FROM crime_updates WHERE crime_id = ? ORDER BY update_timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, crimeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CrimeUpdate update = new CrimeUpdate(
                    rs.getInt("crime_id"),
                    rs.getString("update_text"),
                    rs.getString("updated_by")
                );
                update.setUpdateId(rs.getInt("update_id"));
                update.setUpdateTimestamp(rs.getTimestamp("update_timestamp").toLocalDateTime());
                updates.add(update);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updates;
    }

    // Enhanced analytics methods
    public DefaultCategoryDataset getYearlyTrends() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT YEAR(date_time) as year, COUNT(*) as count FROM crime GROUP BY YEAR(date_time) ORDER BY year";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                dataset.addValue(rs.getInt("count"), "Crimes", String.valueOf(rs.getInt("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public DefaultCategoryDataset getLocationAnalysis() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT area, COUNT(*) as count FROM crime WHERE area IS NOT NULL GROUP BY area ORDER BY count DESC LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                dataset.addValue(rs.getInt("count"), "Crimes", rs.getString("area"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public DefaultPieDataset<String> getStatusDistribution() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String query = "SELECT status, COUNT(*) as count FROM crime GROUP BY status";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                dataset.setValue(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public DefaultCategoryDataset getTypeTrends() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT type, COUNT(*) as count FROM crime GROUP BY type ORDER BY count DESC LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                dataset.addValue(rs.getInt("count"), "Crimes", rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public DefaultCategoryDataset getPriorityAnalysis() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT priority_level, COUNT(*) as count FROM crime GROUP BY priority_level";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                dataset.addValue(rs.getInt("count"), "Crimes", rs.getString("priority_level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public DefaultCategoryDataset getMonthlyTrends() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT MONTH(date_time) as month, COUNT(*) as count FROM crime GROUP BY MONTH(date_time)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                dataset.addValue(rs.getInt("count"), "Crimes", 
                    DateUtils.getMonthName(rs.getInt("month"))); // Use DateUtils
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public boolean addCrime(Crime crime) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CRIME, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, crime.getType());
            stmt.setString(2, crime.getLocation());
            stmt.setString(3, crime.getArea());
            stmt.setTimestamp(4, Timestamp.valueOf(crime.getDateTime()));
            stmt.setString(5, crime.getStatus());
            stmt.setString(6, crime.getDescription());
            stmt.setString(7, crime.getReporterName());
            stmt.setString(8, crime.getReporterContact());
            stmt.setString(9, crime.getPriorityLevel());
            stmt.setString(10, crime.getAssignedOfficer());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    crime.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCrime(Crime crime) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CRIME)) {
            
            stmt.setString(1, crime.getType());
            stmt.setString(2, crime.getLocation());
            stmt.setString(3, crime.getArea());
            stmt.setString(4, crime.getStatus());
            stmt.setString(5, crime.getDescription());
            stmt.setString(6, crime.getPriorityLevel());
            stmt.setString(7, crime.getAssignedOfficer());
            stmt.setInt(8, crime.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCrime(int crimeId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CRIME)) {
            
            stmt.setInt(1, crimeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Crime getCrimeById(int crimeId) {
        String query = "SELECT * FROM crime WHERE crime_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, crimeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCrime(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Crime> getAllCrimes() {
        List<Crime> crimes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            
            while (rs.next()) {
                crimes.add(mapResultSetToCrime(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crimes;
    }

    public List<Crime> searchCrimesAdvanced(String keyword) {
        List<Crime> crimes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_CRIMES)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                crimes.add(mapResultSetToCrime(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crimes;
    }

    private Crime mapResultSetToCrime(ResultSet rs) throws SQLException {
        Crime crime = new Crime(
            rs.getString("type"),
            rs.getString("location"),
            rs.getTimestamp("date_time").toLocalDateTime(),
            rs.getString("status"),
            rs.getString("description")
        );
        
        crime.setId(rs.getInt("crime_id"));
        crime.setArea(rs.getString("area"));
        crime.setReporterName(rs.getString("reporter_name"));
        crime.setReporterContact(rs.getString("reporter_contact"));
        crime.setPriorityLevel(rs.getString("priority_level"));
        crime.setAssignedOfficer(rs.getString("assigned_officer"));
        crime.setEvidenceCollected(rs.getBoolean("evidence_collected"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            crime.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            crime.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return crime;
    }
}