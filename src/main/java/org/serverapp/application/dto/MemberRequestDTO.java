package org.serverapp.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class MemberRequestDTO {
    @NotNull
    private String name;

    @NotNull
    private UUID positionId;

    @JsonIgnore
    private MultipartFile profileImage;

    private UUID superiorId;

    private List<ReportRequestDTO> reports;
}


