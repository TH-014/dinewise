// ManagerApplicationRequestDTO.java
package com.example.dinewise.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class ManagerApplicationRequestDTO {
    @NotBlank
    private String stdId;
    private LocalDate appliedMonth; // Must be first day of month YYYY-MM-01

    // Getters and Setters

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public LocalDate getAppliedMonth() {
        return appliedMonth;
    }
    public void setAppliedMonth(LocalDate appliedMonth) {
        this.appliedMonth = appliedMonth;
    }

    
}

