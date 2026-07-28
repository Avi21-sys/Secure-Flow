package com.secureflow.secureflow_backend.project.service;

import com.secureflow.secureflow_backend.organization.entity.Organization;
import com.secureflow.secureflow_backend.organization.repository.OrganizationRepository;
import com.secureflow.secureflow_backend.project.dto.CreateProjectRequest;
import com.secureflow.secureflow_backend.project.dto.ProjectResponse;
import com.secureflow.secureflow_backend.project.entity.Project;
import com.secureflow.secureflow_backend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public ProjectResponse createProject(CreateProjectRequest request) {

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new RuntimeException("Organization not found")
                );

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .applicationUrl(request.getApplicationUrl())
                .organization(organization)
                .build();

        Project savedProject = projectRepository.save(project);

        return mapToResponse(savedProject);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found")
                );

        return mapToResponse(project);
    }

    @Override
    public List<ProjectResponse> getProjectsByOrganization(Long organizationId) {

        return projectRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProjectResponse mapToResponse(Project project) {

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .applicationUrl(project.getApplicationUrl())
                .status(project.getStatus())
                .organizationId(project.getOrganization().getId())
                .organizationName(project.getOrganization().getName())
                .build();
    }
}
