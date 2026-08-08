package com.secureflow.secureflow_backend.audit.entity;

import com.secureflow.secureflow_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    @Enumerated(EnumType.STRING)
    private AuditAction action;



    private String entityName;



    private Long entityId;



    @Column(length = 1000)
    private String description;



    private LocalDateTime createdAt;



    @PrePersist
    public void onCreate(){

        createdAt = LocalDateTime.now();

    }

}
