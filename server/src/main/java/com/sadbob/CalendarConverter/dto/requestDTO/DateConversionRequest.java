package com.sadbob.CalendarConverter.dto.requestDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DateConversionRequest(
        @NotBlank(message = "Calendar type is required")
        String calendarType, // "greg", "eth", "hijri"

        @NotBlank(message = "Date is required")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in YYYY-MM-DD format")
        String date,

        String targetCalendar
) {}
