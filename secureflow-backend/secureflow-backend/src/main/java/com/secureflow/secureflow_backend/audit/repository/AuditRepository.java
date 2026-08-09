package com.secureflow.secureflow_backend.audit.repository;

import com.secureflow.secureflow_backend.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByEntityName(String entityName);
}
