package com.example.dinewise.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dues")
public class Due {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "std_id", nullable = false)
    private String stdId;

    @Column(name = "last_paid_date")
    private LocalDate lastPaidDate;

    @Column(name = "total_due", nullable = false)
    private double totalDue = 0.0;

    // Getters and Setters
    public Long getId() { return id; }
    public String getStdId() { return stdId; }
    public void setStdId(String stdId) { this.stdId = stdId; }
    public LocalDate getLastPaidDate() { return lastPaidDate; }
    public void setLastPaidDate(LocalDate lastPaidDate) { this.lastPaidDate = lastPaidDate; }
    public double getTotalDue() { return totalDue; }
    public void setTotalDue(double totalDue) { this.totalDue = totalDue; }

    public void addToDue(double amount) { this.totalDue += amount; }
    public void subtractFromDue(double amount) { this.totalDue -= amount; }
}
