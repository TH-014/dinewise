package com.example.dinewise.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import com.example.dinewise.model.Stock;



@Entity
@Table(name = "daily_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    private Double quantityUsed;

    @Enumerated(EnumType.STRING)
    private MealType usedFor;

    private String usedBy;

    private LocalDateTime usedAt = LocalDateTime.now();
}

