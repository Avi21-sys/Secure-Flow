package com.secureflow.secureflow_backend.incident.service;

import com.secureflow.secureflow_backend.audit.entity.AuditAction;
import com.secureflow.secureflow_backend.audit.service.AuditService;
import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.common.security.SecurityUtils;
import com.secureflow.secureflow_backend.incident.dto.CreateIncidentRequest;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;
import com.secureflow.secureflow_backend.incident.dto.UpdateIncidentRequest;
import com.secureflow.secureflow_backend.incident.entity.Incident;
import com.secureflow.secureflow_backend.incident.entity.IncidentSeverity;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import com.secureflow.secureflow_backend.incident.repository.IncidentRepository;
import com.secureflow.secureflow_backend.incident.specification.IncidentSpecification;
import com.secureflow.secureflow_backend.user.entity.User;
import com.secureflow.secureflow_backend.user.repository.UserRepository;
import com.secureflow.secureflow_backend.vulnerability.dto.PagedResponse;
import com.secureflow.secureflow_backend.vulnerability.entity.Vulnerability;
import com.secureflow.secureflow_backend.vulnerability.repository.VulnerabilityRespository;
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
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    private final VulnerabilityRespository vulnerabilityRepository;

    private final UserRepository userRepository;

    private final AuditService auditService;



    @Override
    public IncidentResponse createIncident(
            CreateIncidentRequest request
    ) {


        Vulnerability vulnerability =
                vulnerabilityRepository.findById(
                                request.getVulnerabilityId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Vulnerability not found")
                        );


        User assignedUser = null;


        if(request.getAssignedToId() != null){

            assignedUser =
                    userRepository.findById(
                                    request.getAssignedToId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "User not found"
                                    )
                            );

        }



        Incident incident = Incident.builder()

                .title(request.getTitle())

                .description(request.getDescription())

                .severity(request.getSeverity())

                .vulnerability(vulnerability)

                .assignedTo(assignedUser)

                .build();



        Incident savedIncident =
                incidentRepository.save(incident);

        auditService.logActivity(

                SecurityUtils.getCurrentUserId(),

                AuditAction.CREATE,

                "INCIDENT",

                savedIncident.getId(),

                "Created incident: "
                        + savedIncident.getTitle()

        );


        return mapToResponse(savedIncident);

    }


    @Override
    public List<IncidentResponse> getAllIncidents(){


        return incidentRepository.findAll()

                .stream()

                .map(this::mapToResponse)

                .toList();

    }


    @Override
    public IncidentResponse getIncidentById(Long id){


        Incident incident =
                incidentRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Incident not found"
                                )
                        );


        return mapToResponse(incident);

    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<IncidentResponse> getIncidents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            IncidentStatus status,
            IncidentSeverity severity,
            Long vulnerabilityId,
            Long assignedToId
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 10 : Math.min(size, 100);

        String safeSortBy = validateIncidentSortField(sortBy);

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sortDir)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(direction, safeSortBy)
        );

        var specification = IncidentSpecification.filter(
                status,
                severity,
                vulnerabilityId,
                assignedToId
        );

        Page<Incident> incidentPage =
                incidentRepository.findAll(specification, pageable);

        List<IncidentResponse> content = incidentPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PagedResponse.<IncidentResponse>builder()
                .content(content)
                .page(incidentPage.getNumber())
                .size(incidentPage.getSize())
                .totalElements(incidentPage.getTotalElements())
                .totalPages(incidentPage.getTotalPages())
                .first(incidentPage.isFirst())
                .last(incidentPage.isLast())
                .build();
    }


    @Override
    public List<IncidentResponse> getByVulnerability(
            Long vulnerabilityId
    ){


        return incidentRepository
                .findByVulnerabilityId(vulnerabilityId)

                .stream()

                .map(this::mapToResponse)

                .toList();

    }



    @Override
    public void deleteIncident(Long id){

        Incident incident =
                incidentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Incident not found"
                                )
                        );


        incidentRepository.delete(incident);

        auditService.logActivity(

                SecurityUtils.getCurrentUserId(),

                AuditAction.DELETE,

                "INCIDENT",

                id,

                "Deleted incident: "
                        + incident.getTitle()

        );


    }

    @Override
    public IncidentResponse updateIncident(
            Long id,
            UpdateIncidentRequest request
    ) {


        Incident incident =
                incidentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Incident not found with id: " + id
                                )
                        );


        incident.setStatus(request.getStatus());


        if(request.getAssignedToId() != null){

            User user =
                    userRepository.findById(
                                    request.getAssignedToId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "User not found"
                                    )
                            );

            incident.setAssignedTo(user);
        }


        if(request.getResolutionNotes() != null){

            incident.setResolutionNotes(
                    request.getResolutionNotes()
            );
        }


        if(request.getStatus()
                == IncidentStatus.RESOLVED
        ){

            incident.setResolvedAt(
                    LocalDateTime.now()
            );

        }


        Incident updated =
                incidentRepository.save(incident);

        auditService.logActivity(

                SecurityUtils.getCurrentUserId(),

                AuditAction.UPDATE,

                "INCIDENT",

                updated.getId(),

                "Updated incident status to "
                        + updated.getStatus()

        );

        return mapToResponse(updated);
    }

    private String validateIncidentSortField(String sortBy) {
        List<String> allowedFields = List.of(
                "id",
                "title",
                "severity",
                "status",
                "createdAt",
                "resolvedAt"
        );

        if (sortBy == null || !allowedFields.contains(sortBy)) {
            return "createdAt";
        }

        return sortBy;
    }

    private IncidentResponse mapToResponse(
            Incident incident
    ){


        return IncidentResponse.builder()

                .id(incident.getId())

                .title(incident.getTitle())

                .description(incident.getDescription())

                .severity(incident.getSeverity())

                .status(incident.getStatus())

                .vulnerabilityId(
                        incident.getVulnerability().getId()
                )

                .vulnerabilityTitle(
                        incident.getVulnerability().getTitle()
                )

                .assignedUserId(
                        incident.getAssignedTo() != null ?
                                incident.getAssignedTo().getId()
                                :
                                null
                )

                .assignedUserName(
                        incident.getAssignedTo() != null ?
                                incident.getAssignedTo().getName()
                                :
                                null
                )

                .createdAt(
                        incident.getCreatedAt()
                )

                .resolvedAt(
                        incident.getResolvedAt()
                )

                .resolutionNotes(incident.getResolutionNotes())

                .build();

    }


}
