package com.secureflow.secureflow_backend.organization.entity;

import com.secureflow.secureflow_backend.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String industry;

    @Column(nullable = false)
    private String email;

    private String phone;

    private String website;

    private String address;

    @OneToMany(
            mappedBy = "organization",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Project> projects = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
