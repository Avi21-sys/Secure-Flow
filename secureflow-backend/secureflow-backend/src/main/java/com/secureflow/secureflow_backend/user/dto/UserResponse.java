package com.secureflow.secureflow_backend.user.dto;

import com.secureflow.secureflow_backend.user.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Role role;

    private boolean enabled;

    private LocalDateTime createdAt;
}
