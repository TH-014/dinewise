package com.example.dinewise.dto.response;

import com.example.dinewise.model.ApplicationStatus;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class ApplicationStatusDto {
    private LocalDate appliedMonth;
    private ApplicationStatus status;
    private ZonedDateTime reviewedAt;

    public ApplicationStatusDto(LocalDate appliedMonth, ApplicationStatus status, ZonedDateTime reviewedAt) {
        this.appliedMonth = appliedMonth;
        this.status = status;
        this.reviewedAt = reviewedAt;
    }

    public LocalDate getAppliedMonth() {
        return appliedMonth;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public ZonedDateTime getReviewedAt() {
        return reviewedAt;
    }
}
