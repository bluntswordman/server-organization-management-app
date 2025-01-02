package org.serverapp.presentation.controller;

import lombok.AllArgsConstructor;
import org.serverapp.application.PositionService;
import org.serverapp.application.dto.OrganizationChart;
import org.serverapp.application.dto.PositionRequestDTO;
import org.serverapp.application.dto.PositionResponseDTO;
import org.serverapp.application.dto.ResponseBrowsePositionDTO;
import org.serverapp.domain.entity.Position;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/positions")
@AllArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @GetMapping
    public ResponseEntity<List<PositionResponseDTO>> getAllPositions() {
        List<Position> positions = positionService.getAllPositions();
        List<PositionResponseDTO> response = positions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> getPositionById(@PathVariable UUID id) {
        Position position = positionService.getPositionById(id);
        PositionResponseDTO responseDTO = mapToResponseDTO(position);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<PositionResponseDTO> createPosition(@RequestBody PositionRequestDTO positionRequestDTO) {
        Position createdPosition = positionService.createPosition(positionRequestDTO);
        PositionResponseDTO responseDTO = mapToResponseDTO(createdPosition);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/organization-chart")
    public ResponseEntity<OrganizationChart> getOrganizationChart() {
        OrganizationChart chart = positionService.getOrganizationChart();
        return ResponseEntity.ok(chart);
    }

    @GetMapping("/browse")
    public ResponseEntity<List<ResponseBrowsePositionDTO>> getBrowsePositions() {
        List<ResponseBrowsePositionDTO> positions = positionService.browsePositions();
        return ResponseEntity.ok(positions);
    }

    private PositionResponseDTO mapToResponseDTO(Position position) {
        PositionResponseDTO responseDTO = new PositionResponseDTO();
        responseDTO.setId(position.getId());
        responseDTO.setName(position.getName());
        responseDTO.setDescription(position.getDescription());
        responseDTO.setParentId(position.getParent() != null ? position.getParent().getId() : null);
        responseDTO.setSubordinates(position.getSubordinates().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList()));
        return responseDTO;
    }

}
