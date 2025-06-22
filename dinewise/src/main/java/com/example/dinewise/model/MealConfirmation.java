package com.example.dinewise.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "meal_confirmations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"std_id", "meal_date"})
})
public class MealConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "std_id", nullable = false)
    private String stdId;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Column(name = "will_lunch", nullable = false)
    private boolean willLunch;

    @Column(name = "will_dinner", nullable = false)
    private boolean willDinner;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // @ManyToOne
    // @JoinColumn(name = "std_id")
    // private Student student;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // public Student getStudent() {
    //     return student;
    // }

    // public void setStudent(Student student) {
    //     this.student = student;
    // }


    @Override
    public String toString() {
        return "MealConfirmation{" +
                "id=" + id +
                ", stdId='" + stdId + '\'' +
                ", mealDate=" + mealDate +
                ", willLunch=" + willLunch +
                ", willDinner=" + willDinner +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealConfirmation)) return false;

        MealConfirmation that = (MealConfirmation) o;

        if (willLunch != that.willLunch) return false;
        if (willDinner != that.willDinner) return false;
        if (!id.equals(that.id)) return false;
        if (!stdId.equals(that.stdId)) return false;
        return mealDate.equals(that.mealDate);
    }

   

}

