package com.secureflow.secureflow_backend.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStatusRequest {

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;
}
