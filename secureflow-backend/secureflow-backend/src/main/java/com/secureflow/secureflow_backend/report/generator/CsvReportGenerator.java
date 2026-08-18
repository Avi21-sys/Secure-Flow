package com.secureflow.secureflow_backend.report.generator;

import com.secureflow.secureflow_backend.report.dto.ReportResponse;
import org.springframework.stereotype.Component;

@Component
public class CsvReportGenerator {

    public byte[] generate(
            ReportResponse report
    ){


        String csv =

                "Metric,Value\n"

                        + "Project,"
                        + report.getProjectName()
                        + "\n"

                        + "Risk Score,"
                        + report.getRiskScore()
                        + "\n"

                        + "Critical Vulnerabilities,"
                        + report.getCriticalVulnerabilities()
                        + "\n"

                        + "High Vulnerabilities,"
                        + report.getHighVulnerabilities()
                        + "\n"

                        + "Medium Vulnerabilities,"
                        + report.getMediumVulnerabilities()
                        + "\n"

                        + "Low Vulnerabilities,"
                        + report.getLowVulnerabilities()
                        + "\n"

                        + "Open Incidents,"
                        + report.getOpenIncidents()
                        + "\n"

                        + "Resolved Incidents,"
                        + report.getResolvedIncidents();


        return csv.getBytes();

    }

}
