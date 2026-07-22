package com.secureflow.secureflow_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.DEVELOPER;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
