package com.secureflow.secureflow_backend.project.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    private String name;

    private String description;

    private String applicationUrl;

    private Long organizationId;
}
