package com.secureflow.secureflow_backend.audit.service;

import com.secureflow.secureflow_backend.audit.dto.AuditResponse;
import com.secureflow.secureflow_backend.audit.dto.CreateAuditRequest;
import com.secureflow.secureflow_backend.audit.entity.AuditLog;
import com.secureflow.secureflow_backend.audit.repository.AuditRepository;
import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.user.entity.User;
import com.secureflow.secureflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService{

    private final AuditRepository auditRepository;

    private  final UserRepository userRepository;

    @Override
    public AuditResponse createAudit(CreateAuditRequest request){

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        AuditLog audit = AuditLog.builder()
                .user(user)
                .action(request.getAction())
                .entityName(request.getEntityName())
                .entityId(request.getEntityId())
                .description(request.getDescription())
                .build();

        AuditLog saved = auditRepository.save(audit);

        return mapToResponse(saved);
    }

    @Override
    public List<AuditResponse> getAllAudits(){

        return auditRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AuditResponse> getUserAudits(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        return auditRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AuditResponse> getEntityAudits(String entityName){

        return auditRepository.findByEntityName(entityName).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AuditResponse mapToResponse(AuditLog audit){

        return AuditResponse.builder()
                .id(audit.getId())

                .userId(
                        audit.getUser().getId()
                )

                .username(
                        audit.getUser().getName()
                )

                .action(
                        audit.getAction()
                )

                .entityName(
                        audit.getEntityName()
                )

                .entityId(
                        audit.getEntityId()
                )

                .description(
                        audit.getDescription()
                )

                .createdAt(
                        audit.getCreatedAt()
                )

                .build();
    }
}
