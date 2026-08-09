package com.secureflow.secureflow_backend.audit.dto;

import com.secureflow.secureflow_backend.audit.entity.AuditAction;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditResponse {

    private Long id;


    private Long userId;


    private String username;


    private AuditAction action;


    private String entityName;


    private Long entityId;


    private String description;


    private LocalDateTime createdAt;
}
