package com.example.crm;

import com.example.crm.dto.IncidentExtraction;
import com.example.crm.entity.IncidentReport;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU KHỞI TẠO ===");
        
        // 1. Giả lập quá trình bóc tách từ LLM ra Record DTO
        System.out.println("\n1. Khởi tạo DTO Record từ dữ liệu thô của LLM...");
        IncidentExtraction extraction = new IncidentExtraction(
                "Nguyễn Văn A",
                "0987654321",
                "Xe bị nổ lốp trên đường cao tốc",
                "Cao tốc Hà Nội - Hải Phòng",
                LocalDateTime.now(),
                "HIGH"
        );
        System.out.println("-> DTO tạo thành công:");
        System.out.println(extraction);
        
        // 2. Chuyển đổi an toàn từ DTO sang JPA Entity
        System.out.println("\n2. Thực hiện mapping từ DTO sang JPA Entity...");
        IncidentReport report = new IncidentReport(
                extraction.driverName(),
                extraction.driverPhone(),
                extraction.incidentDescription(),
                extraction.location(),
                extraction.reportedTime(),
                extraction.severityLevel()
        );
        
        System.out.println("-> Entity tạo thành công:");
        System.out.println(report);
        
        System.out.println("\n=== KIỂM TRA HOÀN TẤT. KHÔNG CÓ LỖI RUNTIME ===");
    }
}
