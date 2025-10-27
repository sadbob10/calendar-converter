package com.sadbob.CalendarConverter.dto.requestDTO.age;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for calculating age from birth date")
public record AgeCalculationRequest(
        @Schema(description = "Calendar type of the birth date",
                example = "GREGORIAN",
                allowableValues = {"GREGORIAN", "ETHIOPIAN", "HIJRI"})
        @NotBlank(message = "Birth calendar type is required")
        String calendarType,

        @Schema(description = "Birth date in YYYY-MM-DD format", example = "1990-05-15")
        @NotBlank(message = "Birth date is required")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date must be in YYYY-MM-DD format")
        String birthDate,

        @Schema(description = "Target calendar for age display (optional)",
                example = "ETHIOPIAN",
                allowableValues = {"GREGORIAN", "ETHIOPIAN", "HIJRI"})
        String targetCalendar
) {}