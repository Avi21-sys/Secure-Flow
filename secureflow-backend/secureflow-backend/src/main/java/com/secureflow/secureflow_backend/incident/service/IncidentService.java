package com.secureflow.secureflow_backend.incident.service;

import com.secureflow.secureflow_backend.incident.dto.CreateIncidentRequest;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;

import java.util.List;

public interface IncidentService {

    IncidentResponse createIncident(CreateIncidentRequest request);

    List<IncidentResponse> getAllIncidents();

    IncidentResponse getIncidentById(Long id);

    List<IncidentResponse> getByVulnerability(Long vulnerabilityId);

    void deleteIncident(Long id);
}
