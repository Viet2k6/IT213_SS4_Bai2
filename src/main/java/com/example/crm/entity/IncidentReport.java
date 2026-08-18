package com.example.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_reports")
public class IncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "driver_phone", length = 20)
    private String driverPhone;

    @Column(name = "incident_description", nullable = false, columnDefinition = "TEXT")
    private String incidentDescription;

    @Column(name = "location")
    private String location;

    @Column(name = "reported_time", nullable = false)
    private LocalDateTime reportedTime;

    @Column(name = "severity_level", length = 50)
    private String severityLevel;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public IncidentReport() {
    }

    public IncidentReport(String driverName, String driverPhone, String incidentDescription, 
                          String location, LocalDateTime reportedTime, String severityLevel) {
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.incidentDescription = incidentDescription;
        this.location = location;
        this.reportedTime = reportedTime;
        this.severityLevel = severityLevel;
    }
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
    public String getIncidentDescription() { return incidentDescription; }
    public void setIncidentDescription(String incidentDescription) { this.incidentDescription = incidentDescription; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getReportedTime() { return reportedTime; }
    public void setReportedTime(LocalDateTime reportedTime) { this.reportedTime = reportedTime; }
    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "IncidentReport{" +
                "id=" + id +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", incidentDescription='" + incidentDescription + '\'' +
                ", location='" + location + '\'' +
                ", reportedTime=" + reportedTime +
                ", severityLevel='" + severityLevel + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
