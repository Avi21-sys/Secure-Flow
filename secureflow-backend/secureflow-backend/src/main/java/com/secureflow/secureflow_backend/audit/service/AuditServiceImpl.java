package com.secureflow.secureflow_backend.audit.service;

import com.secureflow.secureflow_backend.audit.dto.AuditResponse;
import com.secureflow.secureflow_backend.audit.dto.CreateAuditRequest;
import com.secureflow.secureflow_backend.audit.entity.AuditAction;
import com.secureflow.secureflow_backend.audit.entity.AuditLog;
import com.secureflow.secureflow_backend.audit.repository.AuditRepository;
import com.secureflow.secureflow_backend.audit.specification.AuditSpecification;
import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.user.entity.User;
import com.secureflow.secureflow_backend.user.repository.UserRepository;
import com.secureflow.secureflow_backend.vulnerability.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Transactional(readOnly = true)
    public PagedResponse<AuditResponse> getAuditLogs(
            int page,
            int size,
            String sortBy,
            String sortDir,
            AuditAction action,
            String entityName,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "startDate must not be after endDate"
            );
        }

        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 10 : Math.min(size, 100);

        String safeSortBy = validateAuditSortField(sortBy);

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sortDir)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(direction, safeSortBy)
        );

        var specification = AuditSpecification.filter(
                action,
                entityName,
                userId,
                startDate,
                endDate
        );

        Page<AuditLog> auditPage =
                auditRepository.findAll(specification, pageable);

        List<AuditResponse> content = auditPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PagedResponse.<AuditResponse>builder()
                .content(content)
                .page(auditPage.getNumber())
                .size(auditPage.getSize())
                .totalElements(auditPage.getTotalElements())
                .totalPages(auditPage.getTotalPages())
                .first(auditPage.isFirst())
                .last(auditPage.isLast())
                .build();
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

    @Override
    public void logActivity(
            Long userId,
            AuditAction action,
            String entityName,
            Long entityId,
            String description
    ){

        CreateAuditRequest request =
                CreateAuditRequest.builder()

                        .userId(userId)

                        .action(action)

                        .entityName(entityName)

                        .entityId(entityId)

                        .description(description)

                        .build();


        createAudit(request);

    }

    private String validateAuditSortField(String sortBy) {
        List<String> allowedFields = List.of(
                "id",
                "action",
                "entityName",
                "entityId",
                "createdAt"
        );

        if (sortBy == null || !allowedFields.contains(sortBy)) {
            return "createdAt";
        }

        return sortBy;
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
