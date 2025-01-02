package org.serverapp.application.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.serverapp.application.ReportService;
import org.serverapp.application.dto.ReportRequest;
import org.serverapp.domain.entity.Member;
import org.serverapp.domain.entity.Report;
import org.serverapp.domain.repository.MemberRepository;
import org.serverapp.domain.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Report createReport(ReportRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Report report = Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();

        return reportRepository.save(report);
    }

    @Override
    public List<Report> getReportsByMember(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return reportRepository.findByMember(member);
    }

    @Override
    public Report getReportDetails(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

    }
}
