package com.secureflow.secureflow_backend.incident.entity;

import com.secureflow.secureflow_backend.user.entity.User;
import com.secureflow.secureflow_backend.vulnerability.entity.Vulnerability;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(length = 2000)
    private String description;


    @Enumerated(EnumType.STRING)
    private IncidentSeverity severity;


    @Enumerated(EnumType.STRING)
    private IncidentStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vulnerability_id")
    private Vulnerability vulnerability;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(length = 2000)
    private String resolutionNotes;


    private LocalDateTime createdAt;


    private LocalDateTime resolvedAt;


    @PrePersist
    public void onCreate(){

        createdAt = LocalDateTime.now();

        if(status == null){
            status = IncidentStatus.OPEN;
        }

    }
}
