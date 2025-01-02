package org.serverapp.application.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.serverapp.application.PositionService;
import org.serverapp.application.dto.OrganizationChart;
import org.serverapp.application.dto.PositionRequestDTO;
import org.serverapp.application.dto.ResponseBrowsePositionDTO;
import org.serverapp.domain.entity.Position;
import org.serverapp.domain.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public List<ResponseBrowsePositionDTO> browsePositions() {
        List<Position> rootPositions = positionRepository.findAll();

        if (!rootPositions.isEmpty()) {
            return rootPositions.stream().map(position -> {
                ResponseBrowsePositionDTO response = new ResponseBrowsePositionDTO();
                response.setId(position.getId());
                response.setName(position.getName());
                response.setDescription(position.getDescription());
                response.setParentId(position.getParent() != null ? position.getParent().getId().toString() : null);
                return response;
            }).toList();
        }

        return List.of();
    }

    @Override
    public Position getPositionById(UUID id) {
        return positionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Position not found"));
    }

    @Override
    @Transactional
    public Position createPosition(PositionRequestDTO requestDTO) {
        Position parentPosition = null;

        if (requestDTO.getParentId() != null) {
            parentPosition = positionRepository.findById(requestDTO.getParentId()).orElseThrow(() -> new IllegalArgumentException("Parent position not found"));
        }

        Position position = Position.builder().name(requestDTO.getName()).description(requestDTO.getDescription()).parent(parentPosition).build();

        return positionRepository.save(position);
    }

    @Override
    public OrganizationChart getOrganizationChart() {
        List<Position> rootPositions = positionRepository.findByParentIsNull();
        List<OrganizationChart> charts = rootPositions.stream().map(this::buildChart).toList();

        return charts.isEmpty() ? null : charts.get(0);
    }


    private OrganizationChart buildChart(Position position) {
        OrganizationChart chart = new OrganizationChart();
        chart.setName(position.getName());
        chart.setPosition(position.getDescription());
        chart.setSubordinates(position.getSubordinates().stream().map(this::buildChart).collect(Collectors.toList()));
        return chart;
    }
}
