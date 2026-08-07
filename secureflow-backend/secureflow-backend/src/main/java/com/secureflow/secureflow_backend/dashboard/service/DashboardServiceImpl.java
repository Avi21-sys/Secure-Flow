package com.secureflow.secureflow_backend.dashboard.service;

import com.secureflow.secureflow_backend.dashboard.dto.DashboardResponse;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import com.secureflow.secureflow_backend.incident.repository.IncidentRepository;
import com.secureflow.secureflow_backend.project.repository.ProjectRepository;
import com.secureflow.secureflow_backend.vulnerability.entity.Severity;
import com.secureflow.secureflow_backend.vulnerability.repository.VulnerabilityRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;

    private final VulnerabilityRespository vulnerabilityRepository;

    private final IncidentRepository incidentRepository;



    @Override
    public DashboardResponse getDashboard() {


        long totalProjects =
                projectRepository.count();


        long totalVulnerabilities =
                vulnerabilityRepository.count();


        long critical =
                vulnerabilityRepository.countBySeverity(
                        Severity.CRITICAL
                );


        long high =
                vulnerabilityRepository.countBySeverity(
                        Severity.HIGH
                );


        long medium =
                vulnerabilityRepository.countBySeverity(
                        Severity.MEDIUM
                );


        long low =
                vulnerabilityRepository.countBySeverity(
                        Severity.LOW
                );


        long totalIncidents =
                incidentRepository.count();


        long openIncidents =
                incidentRepository.countByStatus(
                        IncidentStatus.OPEN
                );


        long resolvedIncidents =
                incidentRepository.countByStatus(
                        IncidentStatus.RESOLVED
                );



        double riskScore =
                calculateRiskScore(
                        critical,
                        high,
                        medium
                );



        return DashboardResponse.builder()

                .totalProjects(totalProjects)

                .totalVulnerabilities(totalVulnerabilities)

                .criticalVulnerabilities(critical)

                .highVulnerabilities(high)

                .mediumVulnerabilities(medium)

                .lowVulnerabilities(low)

                .totalIncidents(totalIncidents)

                .openIncidents(openIncidents)

                .resolvedIncidents(resolvedIncidents)

                .riskScore(riskScore)

                .build();

    }





    private double calculateRiskScore(
            long critical,
            long high,
            long medium
    ){


        double risk =
                (critical * 5)
                        +
                        (high * 3)
                        +
                        (medium * 1);



        double score =
                100 - risk;



        if(score < 0){
            return 0;
        }


        return score;

    }
}
