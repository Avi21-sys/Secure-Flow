package com.secureflow.secureflow_backend.organization.service;

import com.secureflow.secureflow_backend.organization.dto.CreateOrganizationRequest;
import com.secureflow.secureflow_backend.organization.dto.OrganizationResponse;
import com.secureflow.secureflow_backend.organization.entity.Organization;
import com.secureflow.secureflow_backend.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponse createOrganization(CreateOrganizationRequest request) {
        Organization organization = Organization.builder()
                .name(request.getName())
                .industry(request.getIndustry())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .address(request.getAddress())
                .build();

        Organization savedOrganization = organizationRepository.save(organization);
        return mapToResponse(savedOrganization);

    }

    @Override
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrganizationResponse getOrganizationById(Long id){
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with id: " + id));
        return mapToResponse(organization);
    }

    private OrganizationResponse mapToResponse(Organization organization) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .industry(organization.getIndustry())
                .email(organization.getEmail())
                .phone(organization.getPhone())
                .website(organization.getWebsite())
                .address(organization.getAddress())
                .build();
    }
}
