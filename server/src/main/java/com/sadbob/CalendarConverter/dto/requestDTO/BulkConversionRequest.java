package com.sadbob.CalendarConverter.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BulkConversionRequest {

    @Valid
    @NotEmpty(message = "At least one conversion request is required")
    private List<SingleConversionRequest> conversions;

    @Data
    public static class SingleConversionRequest {

        @NotNull(message = "Source date is required")
        private String date;

        @NotNull(message = "Source calendar type is required")
        private String sourceCalendar;

        @NotNull(message = "Target calendar type is required")
        private String targetCalendar;
    }
}
