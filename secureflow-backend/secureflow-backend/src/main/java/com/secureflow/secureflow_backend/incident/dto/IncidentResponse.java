package com.secureflow.secureflow_backend.incident.dto;

import com.secureflow.secureflow_backend.incident.entity.IncidentSeverity;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentResponse {

    private Long id;


    private String title;


    private String description;


    private IncidentSeverity severity;


    private IncidentStatus status;


    private Long vulnerabilityId;


    private String vulnerabilityTitle;


    private Long assignedUserId;


    private String assignedUserName;


    private LocalDateTime createdAt;


    private LocalDateTime resolvedAt;

}
