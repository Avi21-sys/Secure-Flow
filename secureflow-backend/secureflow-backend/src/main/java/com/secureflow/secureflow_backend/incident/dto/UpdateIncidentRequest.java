package com.secureflow.secureflow_backend.incident.dto;

import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateIncidentRequest {

    @NotNull(message = "Incident status is required")
    private IncidentStatus status;


    private Long assignedToId;


    private String resolutionNotes;
}
