package com.example.dinewise.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class ExpenseRequestDTO {

    private String managerId;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    private LocalDate purchaseDate;
    @NotBlank(message = "Total amount cannot be blank")
    private double totalAmount;

    // Getters and Setters
    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
}
