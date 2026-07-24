package com.secureflow.secureflow_backend.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}
