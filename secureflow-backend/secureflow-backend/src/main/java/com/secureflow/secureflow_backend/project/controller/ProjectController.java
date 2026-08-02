package com.secureflow.secureflow_backend.project.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.project.dto.CreateProjectRequest;
import com.secureflow.secureflow_backend.project.dto.ProjectResponse;
import com.secureflow.secureflow_backend.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ) {

        ProjectResponse project = projectService.createProject(request);
        ApiResponse<ProjectResponse> response = ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project created successfully")
                .data(project)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {

        List<ProjectResponse> projects = projectService.getAllProjects();
        ApiResponse<List<ProjectResponse>> response = ApiResponse.<List<ProjectResponse>>builder()
                .success(true)
                .message("Projects retrieved successfully")
                .data(projects)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @PathVariable Long id
    ) {

        ProjectResponse project = projectService.getProjectById(id);
        ApiResponse<ProjectResponse> response = ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project retrieved successfully")
                .data(project)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectsByOrganization(
            @PathVariable Long organizationId
    ) {

        List<ProjectResponse> projects = projectService.getProjectsByOrganization(organizationId);
        ApiResponse<List<ProjectResponse>> response = ApiResponse.<List<ProjectResponse>>builder()
                .success(true)
                .message("Projects retrieved successfully")
                .data(projects)
                .build();

        return ResponseEntity.ok(response);
    }
}
