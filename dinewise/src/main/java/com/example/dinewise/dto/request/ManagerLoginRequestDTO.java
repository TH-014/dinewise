package com.example.dinewise.dto.request;

// ManagerLoginRequestDTO.java

import jakarta.validation.constraints.NotBlank;

public class ManagerLoginRequestDTO {
    @NotBlank
    private String stdId;

    @NotBlank
    private String password;

    // Getters and Setters
    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}

