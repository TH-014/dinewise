package com.example.dinewise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    private Double quantityAdded;
    private Double totalCost;

    private String addedBy;

    private LocalDateTime addedAt = LocalDateTime.now();
}

