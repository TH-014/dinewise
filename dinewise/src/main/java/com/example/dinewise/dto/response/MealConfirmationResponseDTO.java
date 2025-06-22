package com.example.dinewise.dto.response;

import com.example.dinewise.model.MealConfirmation;
import java.time.Instant;
import java.time.LocalDate;


public class MealConfirmationResponseDTO {

    private Long id; // meal confirmation ID
    private LocalDate mealDate;
    private boolean willLunch;
    private boolean willDinner;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructor
    public MealConfirmationResponseDTO(MealConfirmation mc) {
        this.id = mc.getId();
        this.mealDate = mc.getMealDate();
        this.willLunch = mc.isWillLunch();
        this.willDinner = mc.isWillDinner();
        this.createdAt = mc.getCreatedAt();
        this.updatedAt = mc.getUpdatedAt();
    }

    // Getters
    public Long getId() {
        return id;
    }
    public LocalDate getMealDate() {
        return mealDate;
    }
    public boolean isWillLunch() {
        return willLunch;
    }
    public boolean isWillDinner() {
        return willDinner;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    @Override
    public String toString() {
        return "MealConfirmationResponseDTO{" +
                "id=" + id +
                "mealDate=" + mealDate +
                ", willLunch=" + willLunch +
                ", willDinner=" + willDinner +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}

