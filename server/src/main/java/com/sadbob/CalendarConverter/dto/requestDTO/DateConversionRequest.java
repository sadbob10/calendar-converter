package com.sadbob.CalendarConverter.dto.requestDTO;


import com.sadbob.CalendarConverter.validation.ValidCalendarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DateConversionRequest(
        @NotBlank(message = "Source calendar type is required")
        @ValidCalendarType
        String calendarType,

        @NotBlank(message = "Source date is required")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in YYYY-MM-DD format")
        String date,

        @NotBlank(message = "Target calendar type is required")
        @ValidCalendarType
        String targetCalendar
) {}
