package com.secureflow.secureflow_backend.organization.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponse {

    private Long id;

    private String name;

    private String industry;

    private String email;

    private String phone;

    private String website;

    private String address;
}
