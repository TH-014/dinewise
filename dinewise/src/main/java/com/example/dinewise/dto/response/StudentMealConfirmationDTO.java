// dto/response/StudentMealConfirmationDTO.java
package com.example.dinewise.dto.response;

public class StudentMealConfirmationDTO {
    private String stdId;
    private String name;

    public StudentMealConfirmationDTO(String stdId, String name) {
        this.stdId = stdId;
        this.name = name;
    }

    public String getStdId() {
        return stdId;
    }

    public String getName() {
        return name;
    }
}
