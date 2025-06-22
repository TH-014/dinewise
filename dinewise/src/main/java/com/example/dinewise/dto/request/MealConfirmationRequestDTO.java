package com.example.dinewise.dto.request;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MealConfirmationRequestDTO {

    @NotNull
    private LocalDate mealDate;

    private boolean willLunch;

    private boolean willDinner;

    // Getters and Setters
    public LocalDate getMealDate() {
        return mealDate;
    }
    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }
    public boolean isWillLunch() {
        return willLunch;
    }
    public void setWillLunch(boolean willLunch) {
        this.willLunch = willLunch;
    }
    public boolean isWillDinner() {
        return willDinner;
    }
    public void setWillDinner(boolean willDinner) {
        this.willDinner = willDinner;
    }
    @Override
    public String toString() {
        return "MealConfirmationRequestDTO{" +
                "mealDate=" + mealDate +
                ", willLunch=" + willLunch +
                ", willDinner=" + willDinner +
                '}';
    }
    
}
