package com.secureflow.secureflow_backend.organization.service;

import com.secureflow.secureflow_backend.organization.dto.CreateOrganizationRequest;
import com.secureflow.secureflow_backend.organization.dto.OrganizationResponse;

import java.util.List;

public interface OrganizationService {

    OrganizationResponse createOrganization(CreateOrganizationRequest request);

    List<OrganizationResponse> getAllOrganizations();

    OrganizationResponse getOrganizationById(Long id);
}
