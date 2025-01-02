package org.serverapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID parentId;
    private List<PositionResponseDTO> subordinates;
}

