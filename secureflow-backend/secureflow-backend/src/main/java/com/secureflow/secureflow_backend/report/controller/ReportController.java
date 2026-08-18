package com.secureflow.secureflow_backend.report.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.report.dto.ReportResponse;
import com.secureflow.secureflow_backend.report.generator.CsvReportGenerator;
import com.secureflow.secureflow_backend.report.generator.PdfReportGenerator;
import com.secureflow.secureflow_backend.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private final PdfReportGenerator pdfGenerator;

    private  final CsvReportGenerator csvGenerator;



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

    @GetMapping("/project/{projectId}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','MANAGER')")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long projectId
    ){

        ReportResponse report =
                reportService.generateSecurityReport(projectId);


        byte[] pdf =
                pdfGenerator.generate(report);


        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=security-report.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);

    }

    @GetMapping("/project/{projectId}/csv")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','MANAGER')")
    public ResponseEntity<byte[]> downloadCsv(
            @PathVariable Long projectId
    ){

        ReportResponse report =
                reportService.generateSecurityReport(projectId);


        byte[] csv =
                csvGenerator.generate(report);


        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=security-report.csv"
                )
                .contentType(
                        MediaType.TEXT_PLAIN
                )
                .body(csv);

    }

}
