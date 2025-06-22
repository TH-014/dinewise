package com.example.dinewise.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuDate(LocalDate menuDate);
}

