package com.sadbob.CalendarConverter.dto.requestDTO;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeRequest {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Source calendar type is required")
    private String sourceCalendar;

    @NotNull(message = "Target calendar type is required")
    private String targetCalendar;

    // Optional: step in days (default 1)
    private Integer stepDays = 1;

    // Optional: include weekends (default true)
    private Boolean includeWeekends = true;

    // Validate that end date is after start date
    public boolean isValidRange() {
        return endDate.isAfter(startDate) || endDate.isEqual(startDate);
    }

    public long getTotalDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
