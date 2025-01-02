package org.serverapp.application.dto;

import lombok.Data;

@Data
public class AccountRequestDTO {
    private String email;
    private String name;
    private String role;
    private String registrationSource;
}
