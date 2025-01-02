package org.serverapp.domain.repository;

import org.serverapp.domain.entity.Member;
import org.serverapp.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByMember(Member member);
}
