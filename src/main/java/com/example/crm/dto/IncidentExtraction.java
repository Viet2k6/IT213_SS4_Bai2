package com.example.crm.dto;

import java.time.LocalDateTime;

/**
 * Java Record DTO đại diện cho dữ liệu bóc tách thô từ AI (LLM).
 * Record tự nhiên là bất biến (immutable), giúp đảm bảo an toàn dữ liệu,
 * tránh các rủi ro thay đổi ngoài ý muốn (Defensive Programming).
 */
public record IncidentExtraction(
    String driverName,
    String driverPhone,
    String incidentDescription,
    String location,
    LocalDateTime reportedTime,
    String severityLevel
) {
    // Có thể thêm Compact Constructor để validate dữ liệu đầu vào từ AI
    public IncidentExtraction {
        if (incidentDescription == null || incidentDescription.isBlank()) {
            throw new IllegalArgumentException("Mô tả sự cố không được để trống!");
        }
    }
}
