package com.secureflow.secureflow_backend.report.service;

import com.secureflow.secureflow_backend.report.dto.ReportResponse;

public interface ReportService {

    ReportResponse generateSecurityReport(Long projectId);
}
