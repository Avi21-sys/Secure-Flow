package com.secureflow.secureflow_backend.organization.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.organization.dto.CreateOrganizationRequest;
import com.secureflow.secureflow_backend.organization.dto.OrganizationResponse;
import com.secureflow.secureflow_backend.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request
            ) {

        OrganizationResponse organization = organizationService.createOrganization(request);
        ApiResponse<OrganizationResponse> response = ApiResponse.<OrganizationResponse>builder()
                .success(true)
                .message("Organization created successfully")
                .data(organization)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getAllOrganizations() {
        List<OrganizationResponse> organizations = organizationService.getAllOrganizations();
        ApiResponse<List<OrganizationResponse>> response = ApiResponse.<List<OrganizationResponse>>builder()
                .success(true)
                .message("Organizations retrieved successfully")
                .data(organizations)
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationById(
            @PathVariable Long id){
        OrganizationResponse organization = organizationService.getOrganizationById(id);
        ApiResponse<OrganizationResponse> response = ApiResponse.<OrganizationResponse>builder()
                .success(true)
                .message("Organization retrieved successfully")
                .data(organization)
                .build();
        return ResponseEntity.ok(response);
    }
}
