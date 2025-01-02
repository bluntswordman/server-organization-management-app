package org.serverapp.application;

import org.serverapp.application.dto.OrganizationChart;
import org.serverapp.application.dto.PositionRequestDTO;
import org.serverapp.application.dto.ResponseBrowsePositionDTO;
import org.serverapp.domain.entity.Position;

import java.util.List;
import java.util.UUID;

public interface PositionService {
    List<Position> getAllPositions();

    List<ResponseBrowsePositionDTO> browsePositions();

    Position getPositionById(UUID id);

    Position createPosition(PositionRequestDTO requestDTO);

    OrganizationChart getOrganizationChart();
}
