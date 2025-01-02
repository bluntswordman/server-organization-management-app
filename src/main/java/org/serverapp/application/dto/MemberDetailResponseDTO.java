package org.serverapp.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MemberDetailResponseDTO {
    private UUID id;
    private String name;
    private String profileImage;
    private PositionResponseDTO position;
    private List<ReportResponseDTO> reports;
}
