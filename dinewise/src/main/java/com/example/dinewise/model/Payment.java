package com.example.dinewise.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Data

public class Payment {
    @Id
    private UUID id;

    @Column(name = "std_id")
    private String stdId;
    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "paid_at")
    private ZonedDateTime paidAt;

    // Getters and Setters
}

