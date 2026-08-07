package com.secureflow.secureflow_backend.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalProjects;


    private long totalVulnerabilities;


    private long criticalVulnerabilities;


    private long highVulnerabilities;


    private long mediumVulnerabilities;


    private long lowVulnerabilities;


    private long totalIncidents;


    private long openIncidents;


    private long resolvedIncidents;


    private double riskScore;
}
