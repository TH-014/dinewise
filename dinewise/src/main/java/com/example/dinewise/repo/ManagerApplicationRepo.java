package com.example.dinewise.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.ManagerApplication;

public interface ManagerApplicationRepo extends JpaRepository<ManagerApplication, Long> {
    boolean existsByStdIdAndAppliedMonth(String stdId, LocalDate appliedMonth);
    Optional<ManagerApplication> findTopByStdIdOrderByAppliedMonthDesc(String stdId);
}
