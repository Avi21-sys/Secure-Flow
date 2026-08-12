package com.secureflow.secureflow_backend.report.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {

    private String reportType;

    private String projectName;

    private long totalVulnerabilities;

    private long criticalVulnerabilities;

    private long highVulnerabilities;

    private long mediumVulnerabilities;

    private long lowVulnerabilities;

    private long totalIncidents;

    private long openIncidents;

    private long resolvedIncidents;

    private double riskScore;

    private LocalDateTime generatedAt;
}
