package com.secureflow.secureflow_backend.project.entity;

import com.secureflow.secureflow_backend.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1500)
    private String description;

    @Column(nullable = false)
    private String applicationUrl;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if(status == null){
            status = ProjectStatus.ACTIVE;
        }
    }
}
