package com.example.dinewise.repo;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.ManagerStatus;
import com.example.dinewise.model.MessManager;

public interface MessManagerRepo extends JpaRepository<MessManager, UUID> {
    Optional<MessManager> findByStdIdAndStatus(String stdId, ManagerStatus status);
    Optional<MessManager> findByStdId(String stdId);
}

