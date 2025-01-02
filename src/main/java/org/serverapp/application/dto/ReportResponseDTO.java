package org.serverapp.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReportResponseDTO {
    private UUID id;
    private String title;
    private String content;
}
