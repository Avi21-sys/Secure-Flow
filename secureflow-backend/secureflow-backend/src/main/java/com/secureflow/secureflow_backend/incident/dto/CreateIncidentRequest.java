package com.secureflow.secureflow_backend.incident.dto;

import com.secureflow.secureflow_backend.incident.entity.IncidentSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateIncidentRequest {

    @NotBlank(message = "Incident title is required")
    private String title;


    @NotBlank(message = "Incident description is required")
    private String description;


    @NotNull(message = "Severity is required")
    private IncidentSeverity severity;


    @NotNull(message = "Vulnerability ID is required")
    private Long vulnerabilityId;


    private Long assignedToId;
}
