package org.serverapp.presentation.controller;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.AllArgsConstructor;
import org.serverapp.application.MemberService;
import org.serverapp.application.ReportService;
import org.serverapp.application.dto.MemberDetailResponseDTO;
import org.serverapp.application.dto.MemberRequestDTO;
import org.serverapp.application.dto.MemberResponseDTO;
import org.serverapp.application.dto.ReportRequestDTO;
import org.serverapp.domain.entity.Member;
import org.serverapp.domain.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/v1/members")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<Page<MemberResponseDTO>> getAllMembers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<MemberResponseDTO> members = memberService.getAllMembers(search, page, size);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailResponseDTO> getMemberById(@PathVariable UUID id) {
        return memberService.getMemberDetailById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createMember(
            @RequestParam String name,
            @RequestParam String positionId,
            @RequestParam(required = false) String superiorId,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam Map<String, String> reportsData
    ) throws IOException {
        MemberRequestDTO memberRequest = new MemberRequestDTO();
        memberRequest.setName(name);
        memberRequest.setPositionId(parseUUID(positionId));
        memberRequest.setProfileImage(profileImage);
        memberRequest.setSuperiorId(
                (superiorId == null || "null".equalsIgnoreCase(superiorId))
                        ? null
                        : parseUUID(superiorId)
        );

        System.out.println("reportsData" + reportsData);

        List<ReportRequestDTO> reports = new ArrayList<>();
        reportsData.forEach((key, value) -> {
            System.out.println("Key: " + key);
            if (key.startsWith("reports")) {
                String[] parts = key.split("\\.");
                System.out.println("Parts: " + Arrays.toString(parts));
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));

                    if (index >= reports.size()) {
                        for (int i = reports.size(); i <= index; i++) {
                            reports.add(new ReportRequestDTO());
                        }
                    }

                    if ("title".equals(parts[1])) {
                        reports.get(index).setTitle(value);
                    } else if ("content".equals(parts[1])) {
                        reports.get(index).setContent(value);
                    }
                }
            }
        });
        System.out.println("reports" + reports);
        memberRequest.setReports(reports);

        var member = memberService.saveMember(memberRequest);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<List<Report>> getReportsByMember(@PathVariable UUID id) {
        List<Report> reports = reportService.getReportsByMember(id);
        return ResponseEntity.ok(reports);
    }

    public UUID parseUUID(String uuid) {
        if (uuid == null || "null".equalsIgnoreCase(uuid)) {
            return null;
        }
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestStateException("Invalid UUID format: " + uuid);
        }
    }

}
