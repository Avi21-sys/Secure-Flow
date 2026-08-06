package com.secureflow.secureflow_backend.incident.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.incident.dto.CreateIncidentRequest;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;
import com.secureflow.secureflow_backend.incident.dto.UpdateIncidentRequest;
import com.secureflow.secureflow_backend.incident.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;



    // Create Incident
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<ApiResponse<IncidentResponse>> createIncident(
            @Valid @RequestBody CreateIncidentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<IncidentResponse>builder()
                                .success(true)
                                .message("Incident created successfully")
                                .data(incidentService.createIncident(request))
                                .build()
                );
    }


    // Get All Incidents
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','DEVELOPER','MANAGER')"
    )
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> getAllIncidents(){

        return ResponseEntity.ok(
                ApiResponse.<List<IncidentResponse>>builder()
                        .success(true)
                        .message("Incidents fetched successfully")
                        .data(incidentService.getAllIncidents())
                        .build()
        );

    }



    // Get Incident By ID
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','DEVELOPER','MANAGER')"
    )
    public ResponseEntity<ApiResponse<IncidentResponse>> getIncidentById(
            @PathVariable Long id
    ){


        return ResponseEntity.ok(
                ApiResponse.<IncidentResponse>builder()
                        .success(true)
                        .message("Incident fetched successfully")
                        .data(incidentService.getIncidentById(id))
                        .build()
        );

    }


    // Get Incidents By Vulnerability
    @GetMapping("/vulnerability/{vulnerabilityId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','DEVELOPER','MANAGER')"
    )
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> getByVulnerability(
            @PathVariable Long vulnerabilityId
    ){


        return ResponseEntity.ok(
                ApiResponse.<List<IncidentResponse>>builder()
                        .success(true)
                        .message("Vulnerability incidents fetched successfully")
                        .data(incidentService.getByVulnerability(vulnerabilityId))
                        .build()
        );

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<ApiResponse<IncidentResponse>> updateIncident(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIncidentRequest request
    ){

        IncidentResponse response =
                incidentService.updateIncident(id, request);


        return ResponseEntity.ok(
                ApiResponse.<IncidentResponse>builder()
                        .success(true)
                        .message("Incident updated successfully")
                        .data(response)
                        .build()
        );
    }

    // Delete Incident
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteIncident(
            @PathVariable Long id
    ){


        incidentService.deleteIncident(id);


        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Incident deleted successfully")
                        .data(null)
                        .build()
        );

    }

}
