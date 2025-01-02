package org.serverapp.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ResponseBrowsePositionDTO {
    private UUID id;
    private String name;
    private String description;
    private String parentId;
}
