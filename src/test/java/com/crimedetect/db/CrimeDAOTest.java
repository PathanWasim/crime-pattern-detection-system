package com.crimedetect.db;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.crimedetect.model.Crime;

public class CrimeDAOTest {
    
    private CrimeDAO crimeDAO;
    private Crime testCrime;
    
    @BeforeEach
    void setUp() {
        crimeDAO = new CrimeDAO();
        testCrime = new Crime(
            "Test Crime",
            "Test Location",
            LocalDateTime.now(),
            "Open",
            "Test description for unit testing"
        );
        testCrime.setArea("Test Area");
        testCrime.setReporterName("Test Reporter");
        testCrime.setPriorityLevel("Medium");
    }
    
    @Test
    void testCrimeDAOCreation() {
        assertNotNull(crimeDAO);
    }
    
    @Test
    void testGetAllCrimes() {
        List<Crime> crimes = crimeDAO.getAllCrimes();
        assertNotNull(crimes);
        // Note: This test assumes database is accessible
        // In a real scenario, you'd use a test database or mocking
    }
    
    @Test
    void testSearchCrimes() {
        List<Crime> searchResults = crimeDAO.searchCrimes("Test");
        assertNotNull(searchResults);
    }
    
    @Test
    void testGetTotalCrimes() {
        int totalCrimes = crimeDAO.getTotalCrimes();
        assertTrue(totalCrimes >= 0);
    }
    
    @Test
    void testGetSolvedCases() {
        int solvedCases = crimeDAO.getSolvedCases();
        assertTrue(solvedCases >= 0);
    }
    
    @Test
    void testGetPendingCases() {
        int pendingCases = crimeDAO.getPendingCases();
        assertTrue(pendingCases >= 0);
    }
    
    @Test
    void testGetTopLocation() {
        String topLocation = crimeDAO.getTopLocation();
        assertNotNull(topLocation);
    }
    
    @Test
    void testGetCrimeTypeDistribution() {
        var distribution = crimeDAO.getCrimeTypeDistribution();
        assertNotNull(distribution);
    }
    
    @Test
    void testGetMonthlyTrends() {
        var dataset = crimeDAO.getMonthlyTrends();
        assertNotNull(dataset);
    }
    
    @Test
    void testGetYearlyTrends() {
        var dataset = crimeDAO.getYearlyTrends();
        assertNotNull(dataset);
    }
    
    @Test
    void testGetLocationAnalysis() {
        var dataset = crimeDAO.getLocationAnalysis();
        assertNotNull(dataset);
    }
    
    @Test
    void testGetStatusDistribution() {
        var dataset = crimeDAO.getStatusDistribution();
        assertNotNull(dataset);
    }
    
    @Test
    void testGetTypeTrends() {
        var dataset = crimeDAO.getTypeTrends();
        assertNotNull(dataset);
    }
    
    @Test
    void testGetPriorityAnalysis() {
        var dataset = crimeDAO.getPriorityAnalysis();
        assertNotNull(dataset);
    }
}