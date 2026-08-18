package com.example.crm.dto;

import java.time.LocalDateTime;

public record IncidentExtraction(
    String driverName,
    String driverPhone,
    String incidentDescription,
    String location,
    LocalDateTime reportedTime,
    String severityLevel
) {
    public IncidentExtraction {
        if (incidentDescription == null || incidentDescription.isBlank()) {
            throw new IllegalArgumentException("Mô tả sự cố không được để trống!");
        }
    }
}
