package com.secureflow.secureflow_backend.organization.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrganizationRequest {

    private String name;

    private String industry;

    private String email;

    private String phone;

    private String website;

    private String address;
}
