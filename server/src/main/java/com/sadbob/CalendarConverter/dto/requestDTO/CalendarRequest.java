package com.sadbob.CalendarConverter.dto.requestDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarRequest {

    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 9999, message = "Year cannot exceed 9999")
    private Integer year; // Allow null for current year

    @Min(value = 1, message = "Month must be at least 1")
    @Max(value = 13, message = "Month cannot exceed 13")
    private Integer month; // Allow null for current month

    public CalendarRequest() {}

    public CalendarRequest(Integer year, Integer month) {
        this.year = year;
        this.month = month;
    }

}