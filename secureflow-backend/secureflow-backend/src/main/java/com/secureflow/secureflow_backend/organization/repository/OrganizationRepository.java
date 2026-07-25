package com.secureflow.secureflow_backend.organization.repository;

import com.secureflow.secureflow_backend.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {
}
