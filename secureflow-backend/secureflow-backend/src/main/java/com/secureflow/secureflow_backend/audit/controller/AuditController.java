package com.secureflow.secureflow_backend.audit.controller;

import com.secureflow.secureflow_backend.audit.dto.AuditResponse;
import com.secureflow.secureflow_backend.audit.dto.CreateAuditRequest;
import com.secureflow.secureflow_backend.audit.service.AuditService;
import com.secureflow.secureflow_backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audits")
public class AuditController {

    private final AuditService auditService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<ApiResponse<AuditResponse>> createAudit(
            @Valid @RequestBody CreateAuditRequest request
    ){

        AuditResponse response =
                auditService.createAudit(request);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AuditResponse>builder()
                                .success(true)
                                .message("Audit log created successfully")
                                .data(response)
                                .build()
                );
    }


    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER')"
    )
    public ResponseEntity<ApiResponse<List<AuditResponse>>> getAllAudits(){


        return ResponseEntity.ok(

                ApiResponse.<List<AuditResponse>>builder()

                        .success(true)

                        .message(
                                "Audit logs fetched successfully"
                        )

                        .data(
                                auditService.getAllAudits()
                        )

                        .build()
        );

    }


    @GetMapping("/user/{userId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER')"
    )
    public ResponseEntity<ApiResponse<List<AuditResponse>>> getUserAudits(
            @PathVariable Long userId
    ){


        return ResponseEntity.ok(

                ApiResponse.<List<AuditResponse>>builder()

                        .success(true)

                        .message(
                                "User audit history fetched successfully"
                        )

                        .data(
                                auditService.getUserAudits(userId)
                        )

                        .build()
        );

    }





    @GetMapping("/entity/{entityName}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER')"
    )
    public ResponseEntity<ApiResponse<List<AuditResponse>>> getEntityAudits(
            @PathVariable String entityName
    ){


        return ResponseEntity.ok(

                ApiResponse.<List<AuditResponse>>builder()

                        .success(true)

                        .message(
                                "Entity audit history fetched successfully"
                        )

                        .data(
                                auditService.getEntityAudits(entityName)
                        )

                        .build()
        );

    }

}
