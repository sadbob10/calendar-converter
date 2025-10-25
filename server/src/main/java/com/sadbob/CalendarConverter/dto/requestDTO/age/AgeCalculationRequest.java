package com.sadbob.CalendarConverter.dto.requestDTO.age;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AgeCalculationRequest(
        @NotBlank(message = "Birth calendar type is required")
        String calendarType,

        @NotBlank(message = "Birth date is required")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date must be in YYYY-MM-DD format")
        String birthDate,

        // Add optional target calendar for age calculation
        String targetCalendar
) {}