package com.secureflow.secureflow_backend.audit.dto;

import com.secureflow.secureflow_backend.audit.entity.AuditAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuditRequest {

    @NotNull(message = "User ID is required")
    private Long userId;


    @NotNull(message = "Action is required")
    private AuditAction action;


    @NotBlank(message = "Entity name is required")
    private String entityName;


    private Long entityId;


    private String description;
}
