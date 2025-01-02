package org.serverapp.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReportRequest {
    private String title;
    private String content;
    private UUID memberId;
}
