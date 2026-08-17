package com.secureflow.secureflow_backend.report.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.report.dto.ReportResponse;
import com.secureflow.secureflow_backend.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;



    @GetMapping("/project/{projectId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER')"
    )
    public ResponseEntity<ApiResponse<ReportResponse>> generateProjectReport(
            @PathVariable Long projectId
    ){


        ReportResponse report =
                reportService.generateSecurityReport(projectId);



        return ResponseEntity.ok(

                ApiResponse.<ReportResponse>builder()

                        .success(true)

                        .message(
                                "Security report generated successfully"
                        )

                        .data(report)

                        .build()

        );

    }

}
