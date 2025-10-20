package com.sadbob.CalendarConverter.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkConversionResponse {
    private List<SingleConversionResult> results;
    private BulkSummary summary;
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleConversionResult {
        private String sourceDate;
        private String sourceCalendar;
        private String targetDate;
        private String targetCalendar;
        private String formattedTargetDate;
        private boolean success;
        private String errorMessage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkSummary {
        private int totalRequests;
        private int successfulConversions;
        private int failedConversions;
        private long processingTimeMs;
    }
}
