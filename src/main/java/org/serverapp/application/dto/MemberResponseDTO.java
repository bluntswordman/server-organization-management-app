package org.serverapp.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MemberResponseDTO {
    private UUID id;
    private String name;
    private String position;
}
