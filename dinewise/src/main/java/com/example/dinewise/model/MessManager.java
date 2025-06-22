package com.example.dinewise.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Data
@Table(name = "mess_manager")
public class MessManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "std_id", nullable = false)
    private String stdId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ManagerStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "avg_rating")
    private double avgRating;
}



