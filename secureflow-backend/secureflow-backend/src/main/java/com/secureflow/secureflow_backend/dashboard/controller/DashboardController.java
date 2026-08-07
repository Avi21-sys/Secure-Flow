package com.secureflow.secureflow_backend.dashboard.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.dashboard.dto.DashboardResponse;
import com.secureflow.secureflow_backend.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER')"
    )
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {


        DashboardResponse dashboard =
                dashboardService.getDashboard();


        return ResponseEntity.ok(

                ApiResponse.<DashboardResponse>builder()

                        .success(true)

                        .message(
                                "Dashboard data fetched successfully"
                        )

                        .data(dashboard)

                        .build()

        );

    }
}