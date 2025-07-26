package com.example.dinewise.dto.response;

import java.time.LocalDate;

public class ExpenseResponseDTO {

    private Long id; // expense ID
    private String managerId;
    private String description;
    private LocalDate purchaseDate;
    private double totalAmount;

    // Constructor
    public ExpenseResponseDTO(Long id, String managerId, String description, LocalDate purchaseDate, double totalAmount) {
        this.id = id;
        this.managerId = managerId;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    
}
