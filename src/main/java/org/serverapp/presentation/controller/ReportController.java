package org.serverapp.presentation.controller;

import lombok.AllArgsConstructor;
import org.serverapp.application.ReportService;
import org.serverapp.application.dto.ReportRequest;
import org.serverapp.domain.entity.Report;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("api/v1/reports")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping()
    public ResponseEntity<Report> createReport(@RequestBody ReportRequest request) {
        Report report = reportService.createReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportDetail(@PathVariable UUID id) {
        Report report = reportService.getReportDetails(id);
        return ResponseEntity.ok(report);
    }
}
