package com.secureflow.secureflow_backend.project.dto;

import com.secureflow.secureflow_backend.project.entity.ProjectStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;

    private String name;

    private String description;

    private String applicationUrl;

    private ProjectStatus status;

    private Long organizationId;

    private String organizationName;
}
