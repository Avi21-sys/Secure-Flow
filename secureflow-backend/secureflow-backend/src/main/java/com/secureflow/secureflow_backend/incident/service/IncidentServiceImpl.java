package com.secureflow.secureflow_backend.incident.service;

import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.incident.dto.CreateIncidentRequest;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;
import com.secureflow.secureflow_backend.incident.entity.Incident;
import com.secureflow.secureflow_backend.incident.repository.IncidentRepository;
import com.secureflow.secureflow_backend.user.entity.User;
import com.secureflow.secureflow_backend.user.repository.UserRepository;
import com.secureflow.secureflow_backend.vulnerability.entity.Vulnerability;
import com.secureflow.secureflow_backend.vulnerability.repository.VulnerabilityRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    private final VulnerabilityRespository vulnerabilityRepository;

    private final UserRepository userRepository;



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

        incidentRepository.deleteById(id);

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

                .build();

    }


}
