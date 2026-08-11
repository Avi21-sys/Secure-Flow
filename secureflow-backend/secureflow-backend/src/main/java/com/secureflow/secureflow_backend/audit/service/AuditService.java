package com.secureflow.secureflow_backend.audit.service;

import com.secureflow.secureflow_backend.audit.dto.AuditResponse;
import com.secureflow.secureflow_backend.audit.dto.CreateAuditRequest;
import com.secureflow.secureflow_backend.audit.entity.AuditAction;

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
}
