package com.secureflow.secureflow_backend.report.service;

import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import com.secureflow.secureflow_backend.incident.repository.IncidentRepository;
import com.secureflow.secureflow_backend.project.entity.Project;
import com.secureflow.secureflow_backend.project.repository.ProjectRepository;
import com.secureflow.secureflow_backend.report.dto.ReportResponse;
import com.secureflow.secureflow_backend.vulnerability.entity.Severity;
import com.secureflow.secureflow_backend.vulnerability.repository.VulnerabilityRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final ProjectRepository projectRepository;

    private final VulnerabilityRespository vulnerabilityRepository;

    private final IncidentRepository incidentRepository;

    @Override
    public ReportResponse generateSecurityReport(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: " + projectId
                        )
                );

        long totalVulnerabilities =
                vulnerabilityRepository.countByProjectId(projectId);

        long critical =
                vulnerabilityRepository
                        .countByProjectIdAndSeverity(
                                projectId,
                                Severity.CRITICAL
                        );

        long high =
                vulnerabilityRepository
                        .countByProjectIdAndSeverity(
                                projectId,
                                Severity.HIGH
                        );

        long medium =
                vulnerabilityRepository
                        .countByProjectIdAndSeverity(
                                projectId,
                                Severity.MEDIUM
                        );

        long low =
                vulnerabilityRepository
                        .countByProjectIdAndSeverity(
                                projectId,
                                Severity.LOW
                        );

        long totalIncidents =
                incidentRepository
                        .countByVulnerabilityProjectId(projectId);

        long openIncidents =
                incidentRepository
                        .countByVulnerabilityProjectIdAndStatus(
                                projectId,
                                IncidentStatus.OPEN
                        );

        long resolvedIncidents =
                incidentRepository
                        .countByVulnerabilityProjectIdAndStatus(
                                projectId,
                                IncidentStatus.RESOLVED
                        );

        double riskScore =
                calculateRiskScore(
                        critical,
                        high,
                        medium
                );

        return ReportResponse.builder()

                .reportType("PROJECT_SECURITY")

                .projectName(project.getName())

                .totalVulnerabilities(totalVulnerabilities)

                .criticalVulnerabilities(critical)

                .highVulnerabilities(high)

                .mediumVulnerabilities(medium)

                .lowVulnerabilities(low)

                .totalIncidents(totalIncidents)

                .openIncidents(openIncidents)

                .resolvedIncidents(resolvedIncidents)

                .riskScore(riskScore)

                .generatedAt(LocalDateTime.now())

                .build();
    }

    private double calculateRiskScore(
            long critical,
            long high,
            long medium
    ) {

        double risk =
                (critical * 5)
                        + (high * 3)
                        + medium;

        double score = 100 - risk;

        return Math.max(score, 0);
    }
}
