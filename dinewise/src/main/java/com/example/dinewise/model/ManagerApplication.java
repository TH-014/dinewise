package com.example.dinewise.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "manager_applications")
public class ManagerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "std_id", nullable = false, length = 10)
    private String stdId;

    @Column(name = "applied_month", nullable = false)
    private LocalDate appliedMonth;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.pending;

    @Column(name = "reviewed_at")
    private ZonedDateTime reviewedAt;
}




