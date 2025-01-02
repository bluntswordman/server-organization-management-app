package org.serverapp.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PositionRequestDTO {
    private String name;
    private String description;
    private UUID parentId;
}
