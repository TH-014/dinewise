package com.example.dinewise.dto.response;

import lombok.Data;

@Data
public class ManagerApplicationResponseDTO {
    private Long id;
    private String stdId;
    private String appliedMonth;
    private String status;
    private String reviewedAt;
}
