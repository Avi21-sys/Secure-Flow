package com.secureflow.secureflow_backend.incident.service;

import com.secureflow.secureflow_backend.incident.dto.CreateIncidentRequest;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;
import com.secureflow.secureflow_backend.incident.dto.UpdateIncidentRequest;

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
}
