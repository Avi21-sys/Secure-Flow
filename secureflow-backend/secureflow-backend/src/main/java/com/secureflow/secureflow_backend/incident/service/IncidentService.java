package com.secureflow.secureflow_backend.incident.service;

import com.secureflow.secureflow_backend.incident.dto.CreateIncidentRequest;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;
import com.secureflow.secureflow_backend.incident.dto.UpdateIncidentRequest;
import com.secureflow.secureflow_backend.incident.entity.IncidentSeverity;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import com.secureflow.secureflow_backend.common.dto.PagedResponse;

import java.util.List;

public interface IncidentService {

    IncidentResponse createIncident(CreateIncidentRequest request);

    List<IncidentResponse> getAllIncidents();

    IncidentResponse getIncidentById(Long id);

    List<IncidentResponse> getByVulnerability(Long vulnerabilityId);

    void deleteIncident(Long id);

    IncidentResponse updateIncident(
            Long id,
            UpdateIncidentRequest request
    );

    PagedResponse<IncidentResponse> getIncidents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            IncidentStatus status,
            IncidentSeverity severity,
            Long vulnerabilityId,
            Long assignedToId
    );
}
