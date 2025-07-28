package com.example.dinewise.repo;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuDate(LocalDate menuDate);
    List<Menu> findByMenuDateAfter(LocalDate date);
    List<Menu> findTop5ByMenuDateLessThanOrderByMenuDateDesc(LocalDate date);
}

