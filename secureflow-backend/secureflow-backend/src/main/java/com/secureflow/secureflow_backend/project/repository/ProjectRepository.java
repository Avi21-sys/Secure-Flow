package com.secureflow.secureflow_backend.project.repository;

import com.secureflow.secureflow_backend.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findByOrganizationId(Long organizationId);
}
