package com.aurafarming.dto;

import java.time.LocalDate;

public record ExportRequestDTO(String reportType, LocalDate fromDate, LocalDate toDate, String district,
        String farmerId,
        String role) {
}
