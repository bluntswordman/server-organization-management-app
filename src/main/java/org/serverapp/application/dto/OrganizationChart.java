package org.serverapp.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationChart {
    private String name;
    private String position;
    private List<OrganizationChart> subordinates;
}
