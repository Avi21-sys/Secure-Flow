package com.secureflow.secureflow_backend.user.dto;

import com.secureflow.secureflow_backend.user.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoleRequest {

    @NotNull(message = "Role is required")
    private Role role;
}
