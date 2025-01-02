package org.serverapp.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReportResponse {
    private UUID id;
    private String title;
    private String content;
    private String memberName;
}
