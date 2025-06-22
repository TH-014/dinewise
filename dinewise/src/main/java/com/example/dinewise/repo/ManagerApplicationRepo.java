package com.example.dinewise.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.ManagerApplication;

public interface ManagerApplicationRepo extends JpaRepository<ManagerApplication, Long> {
    boolean existsByStdIdAndAppliedMonth(String stdId, LocalDate appliedMonth);
}
