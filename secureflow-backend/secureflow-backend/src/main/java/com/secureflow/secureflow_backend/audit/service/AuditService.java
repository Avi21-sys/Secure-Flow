package com.secureflow.secureflow_backend.audit.service;

import com.secureflow.secureflow_backend.audit.dto.AuditResponse;
import com.secureflow.secureflow_backend.audit.dto.CreateAuditRequest;
import com.secureflow.secureflow_backend.audit.entity.AuditAction;
import com.secureflow.secureflow_backend.common.dto.PagedResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {

    AuditResponse createAudit(CreateAuditRequest request);

    List<AuditResponse> getAllAudits();

    List<AuditResponse> getUserAudits(Long userId);

    List<AuditResponse> getEntityAudits(String entityName);

    void logActivity(
            Long userId,
            AuditAction action,
            String entityName,
            Long entityId,
            String description
    );

    PagedResponse<AuditResponse> getAuditLogs(
            int page,
            int size,
            String sortBy,
            String sortDir,
            AuditAction action,
            String entityName,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
