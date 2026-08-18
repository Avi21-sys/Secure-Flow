package com.secureflow.secureflow_backend.report.generator;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.secureflow.secureflow_backend.report.dto.ReportResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfReportGenerator {

    public byte[] generate(
            ReportResponse report
    ) {


        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();


        Document document =
                new Document();


        try {


            PdfWriter.getInstance(
                    document,
                    outputStream
            );


            document.open();


            Font titleFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            18
                    );


            document.add(
                    new Paragraph(
                            "SecureFlow Security Report",
                            titleFont
                    )
            );


            document.add(
                    new Paragraph(
                            "Project: "
                                    + report.getProjectName()
                    )
            );


            document.add(
                    new Paragraph(
                            "Risk Score: "
                                    + report.getRiskScore()
                    )
            );


            document.add(
                    new Paragraph(
                            " "
                    )
            );


            document.add(
                    new Paragraph(
                            "Vulnerability Summary"
                    )
            );


            document.add(
                    new Paragraph(
                            "Critical: "
                                    + report.getCriticalVulnerabilities()
                    )
            );


            document.add(
                    new Paragraph(
                            "High: "
                                    + report.getHighVulnerabilities()
                    )
            );


            document.add(
                    new Paragraph(
                            "Medium: "
                                    + report.getMediumVulnerabilities()
                    )
            );


            document.add(
                    new Paragraph(
                            "Low: "
                                    + report.getLowVulnerabilities()
                    )
            );


            document.add(
                    new Paragraph(
                            " "
                    )
            );


            document.add(
                    new Paragraph(
                            "Incident Summary"
                    )
            );


            document.add(
                    new Paragraph(
                            "Open Incidents: "
                                    + report.getOpenIncidents()
                    )
            );


            document.add(
                    new Paragraph(
                            "Resolved Incidents: "
                                    + report.getResolvedIncidents()
                    )
            );


            document.close();


        } catch(Exception e){

            throw new RuntimeException(
                    "PDF generation failed"
            );

        }


        return outputStream.toByteArray();

    }
}
