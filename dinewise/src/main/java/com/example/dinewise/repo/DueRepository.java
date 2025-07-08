package com.example.dinewise.repo;

import com.example.dinewise.model.Due;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DueRepository extends JpaRepository<Due, Long> {
    Optional<Due> findByStdId(String stdId);
}
