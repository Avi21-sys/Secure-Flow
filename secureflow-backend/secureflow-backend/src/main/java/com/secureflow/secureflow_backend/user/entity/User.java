package com.secureflow.secureflow_backend.user.entity;

import com.secureflow.secureflow_backend.audit.entity.AuditLog;
import com.secureflow.secureflow_backend.notification.entity.Notification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<AuditLog> auditLogs = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Notification> notifications = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.DEVELOPER;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
