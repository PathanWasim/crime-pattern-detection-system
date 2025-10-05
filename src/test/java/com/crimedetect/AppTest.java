package com.crimedetect;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.crimedetect.model.Crime;
import com.crimedetect.model.Admin;
import com.crimedetect.utils.ValidationUtils;

public class AppTest {
    
    private Crime testCrime;
    private Admin testAdmin;
    
    @BeforeEach
    void setUp() {
        testCrime = new Crime(
            "Theft",
            "FC Road",
            LocalDateTime.now(),
            "Open",
            "Test crime description"
        );
        
        testAdmin = new Admin("testuser", "testpass");
    }
    
    @Test
    void testCrimeCreation() {
        assertNotNull(testCrime);
        assertEquals("Theft", testCrime.getType());
        assertEquals("FC Road", testCrime.getLocation());
        assertEquals("Open", testCrime.getStatus());
        assertEquals("Test crime description", testCrime.getDescription());
    }
    
    @Test
    void testCrimeSetters() {
        testCrime.setId(1);
        testCrime.setArea("Central");
        testCrime.setPriorityLevel("High");
        testCrime.setReporterName("John Doe");
        
        assertEquals(1, testCrime.getId());
        assertEquals("Central", testCrime.getArea());
        assertEquals("High", testCrime.getPriorityLevel());
        assertEquals("John Doe", testCrime.getReporterName());
    }
    
    @Test
    void testAdminCreation() {
        assertNotNull(testAdmin);
        assertEquals("testuser", testAdmin.getUsername());
        assertEquals("testpass", testAdmin.getPassword());
    }
    
    @Test
    void testValidationUtils() {
        // Test email validation
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertFalse(ValidationUtils.isValidEmail("invalid-email"));
        
        // Test phone validation
        assertTrue(ValidationUtils.isValidPhone("1234567890"));
        assertFalse(ValidationUtils.isValidPhone("123"));
        
        // Test date time validation
        assertTrue(ValidationUtils.isValidDateTime("2024-01-15 14:30"));
        assertFalse(ValidationUtils.isValidDateTime("invalid-date"));
        
        // Test empty string validation
        assertTrue(ValidationUtils.isNotEmpty("test"));
        assertFalse(ValidationUtils.isNotEmpty(""));
        assertFalse(ValidationUtils.isNotEmpty(null));
        
        // Test priority validation
        assertTrue(ValidationUtils.isValidPriority("High"));
        assertTrue(ValidationUtils.isValidPriority("Low"));
        assertFalse(ValidationUtils.isValidPriority("Invalid"));
        
        // Test status validation
        assertTrue(ValidationUtils.isValidStatus("Open"));
        assertTrue(ValidationUtils.isValidStatus("Solved"));
        assertFalse(ValidationUtils.isValidStatus("Invalid"));
    }
    
    @Test
    void testInputSanitization() {
        String maliciousInput = "<script>alert('xss')</script>";
        String sanitized = ValidationUtils.sanitizeInput(maliciousInput);
        assertFalse(sanitized.contains("<"));
        assertFalse(sanitized.contains(">"));
    }
    
    @Test
    void testCrimeToString() {
        testCrime.setId(1);
        testCrime.setPriorityLevel("Medium");
        String crimeString = testCrime.toString();
        
        assertTrue(crimeString.contains("id=1"));
        assertTrue(crimeString.contains("type='Theft'"));
        assertTrue(crimeString.contains("location='FC Road'"));
        assertTrue(crimeString.contains("status='Open'"));
        assertTrue(crimeString.contains("priority='Medium'"));
    }
    
    @Test
    void testCrimeEnhancedConstructor() {
        Crime enhancedCrime = new Crime(
            "Burglary",
            "Koregaon Park",
            "North",
            LocalDateTime.now(),
            "Under Investigation",
            "House break-in",
            "Jane Smith",
            "High"
        );
        
        assertEquals("Burglary", enhancedCrime.getType());
        assertEquals("Koregaon Park", enhancedCrime.getLocation());
        assertEquals("North", enhancedCrime.getArea());
        assertEquals("Under Investigation", enhancedCrime.getStatus());
        assertEquals("House break-in", enhancedCrime.getDescription());
        assertEquals("Jane Smith", enhancedCrime.getReporterName());
        assertEquals("High", enhancedCrime.getPriorityLevel());
    }
}