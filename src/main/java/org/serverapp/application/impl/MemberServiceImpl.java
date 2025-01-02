package org.serverapp.application.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.serverapp.application.MemberService;
import org.serverapp.application.dto.*;
import org.serverapp.domain.entity.Member;
import org.serverapp.domain.entity.Position;
import org.serverapp.domain.entity.Report;
import org.serverapp.domain.repository.MemberRepository;
import org.serverapp.domain.repository.PositionRepository;
import org.serverapp.domain.repository.ReportRepository;
import org.serverapp.infrastructure.exception.BadRequestException;
import org.serverapp.infrastructure.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final PositionRepository positionRepository;

    @Override
    public Page<MemberResponseDTO> getAllMembers(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Member> memberPage;

        if (search != null && !search.isBlank()) {
            memberPage = memberRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            memberPage = memberRepository.findAll(pageable);
        }

        return memberPage.map(member -> MemberResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .position(member.getPosition().getName())
                .build());
    }

    @Override
    public Optional<MemberDetailResponseDTO> getMemberDetailById(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        return Optional.of(convertToMemberDetailDTO(member));
    }

    @Override
    @Transactional
    public Member saveMember(MemberRequestDTO request) throws IOException {
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new BadRequestException("Position not found"));

        Member superior = null;
        if (request.getSuperiorId() != null) {
            Position superiorPosition = positionRepository.findById(request.getSuperiorId())
                    .orElseThrow(() -> new IllegalArgumentException("Posisi superior tidak ditemukan"));
            List<Member> superiorMembers = memberRepository.findByPosition(superiorPosition);
            if (superiorMembers.isEmpty()) {
                throw new IllegalArgumentException("Superior tidak ditemukan");
            }
            superior = superiorMembers.get(0);
        }

        String profileImagePath = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            profileImagePath = saveProfileImage(request.getProfileImage());
        }

        Member member = Member.builder()
                .name(request.getName())
                .position(position)
                .profileImage(profileImagePath)
                .superior(superior)
                .build();

        Member savedMember = memberRepository.save(member);

        if (request.getReports() != null) {
            List<Report> reports = request.getReports().stream()
                    .map(reportRequest -> {
                        Report report = new Report();
                        report.setTitle(reportRequest.getTitle());
                        report.setContent(reportRequest.getContent());
                        report.setMember(savedMember);
                        return report;
                    }).collect(Collectors.toList());
            reportRepository.saveAll(reports);
        }

        return savedMember;
    }

    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String uploadDir = "src/main/resources/static/images/profile_images";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String uniqueFileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, uniqueFileName);
        Files.write(filePath, profileImage.getBytes());
        return "profile_images/" + uniqueFileName;
    }

    private MemberDetailResponseDTO convertToMemberDetailDTO(Member member) {
        // Map reports to ReportResponseDTO
        List<ReportResponseDTO> reportDTOs = member.getReports().stream()
                .map(report -> ReportResponseDTO.builder()
                        .id(report.getId())
                        .title(report.getTitle())
                        .content(report.getContent())
                        .build())
                .toList();

        return MemberDetailResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .position(convertToPositionDTO(member.getPosition()))
                .reports(reportDTOs) // Include reports here
                .build();
    }


    private PositionResponseDTO convertToPositionDTO(Position position) {
        return PositionResponseDTO.builder()
                .id(position.getId())
                .name(position.getName())
                .description(position.getDescription())
                .parentId(position.getParent() != null ? position.getParent().getId() : null)
                .subordinates(position.getSubordinates().stream()
                        .map(this::convertToPositionDTO)
                        .toList())
                .build();
    }
}
