package com.example.dinewise.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "menus")
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_date", unique = true, nullable = false)
    private LocalDate menuDate;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    // @Column(name = "lunch_items", columnDefinition = "jsonb")
    // @Convert(converter = JsonConverter.class)
    // private List<String> lunchItems = new ArrayList<>();

    // @Column(name = "dinner_items", columnDefinition = "jsonb")
    // @Convert(converter = JsonConverter.class)
    // private List<String> dinnerItems = new ArrayList<>();

    @Column(name = "lunch_items", columnDefinition = "text[]")
    private List<String> lunchItems = new ArrayList<>();

    @Column(name = "dinner_items", columnDefinition = "text[]")
    private List<String> dinnerItems = new ArrayList<>();


    // Getters and setters

    
}
