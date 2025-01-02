package org.serverapp.application;

import org.serverapp.application.dto.ReportRequest;
import org.serverapp.domain.entity.Member;
import org.serverapp.domain.entity.Report;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    Report createReport(ReportRequest request);

    List<Report> getReportsByMember(UUID id);

    Report getReportDetails(UUID reportId);

}
