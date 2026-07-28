package com.secureflow.secureflow_backend.project.service;

import com.secureflow.secureflow_backend.project.dto.CreateProjectRequest;
import com.secureflow.secureflow_backend.project.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request);

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(Long id);

    List<ProjectResponse> getProjectsByOrganization(Long organizationId);
}
