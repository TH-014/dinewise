package com.example.dinewise.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MenuRequestDTO {
    private LocalDate menuDate;
    private List<String> lunchItems;
    private List<String> dinnerItems;

    public MenuRequestDTO() {
        // Default constructor
    }

    // Getters and Setters

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public List<String> getLunchItems() {
        return lunchItems;
    }

    public void setLunchItems(List<String> lunchItems) {
        this.lunchItems = lunchItems;
    }

    public List<String> getDinnerItems() {
        return dinnerItems;
    }

    public void setDinnerItems(List<String> dinnerItems) {
        this.dinnerItems = dinnerItems;
    }

    

}
