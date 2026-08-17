package com.secureflow.secureflow_backend.incident.repository;

import com.secureflow.secureflow_backend.incident.entity.Incident;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByVulnerabilityId(Long vulnerabilityId);

    List<Incident> findByStatus(IncidentStatus status);

    long countByStatus(IncidentStatus status);

    long countByVulnerabilityProjectId(Long projectId);

    long countByVulnerabilityProjectIdAndStatus(
            Long projectId,
            IncidentStatus status
    );
}
