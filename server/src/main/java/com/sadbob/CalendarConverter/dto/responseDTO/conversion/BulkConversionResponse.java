package com.sadbob.CalendarConverter.dto.responseDTO.conversion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for bulk date conversion operations")
public class BulkConversionResponse {

    @Schema(description = "List of individual conversion results")
    private List<SingleConversionResult> results;

    @Schema(description = "Summary statistics of the bulk conversion")
    private BulkSummary summary;

    @Schema(description = "Additional message or error information")
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Single conversion result")
    public static class SingleConversionResult {
        @Schema(description = "Original source date", example = "2024-12-25")
        private String sourceDate;

        @Schema(description = "Source calendar type", example = "GREGORIAN")
        private String sourceCalendar;

        @Schema(description = "Converted target date", example = "2017-04-16")
        private String targetDate;

        @Schema(description = "Target calendar type", example = "ETHIOPIAN")
        private String targetCalendar;

        @Schema(description = "Formatted target date", example = "16 Tahsas 2017")
        private String formattedTargetDate;

        @Schema(description = "Conversion success status")
        private boolean success;

        @Schema(description = "Error message if conversion failed")
        private String errorMessage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Bulk conversion summary")
    public static class BulkSummary {
        @Schema(description = "Total number of conversion requests")
        private int totalRequests;

        @Schema(description = "Number of successful conversions")
        private int successfulConversions;

        @Schema(description = "Number of failed conversions")
        private int failedConversions;

        @Schema(description = "Processing time in milliseconds")
        private long processingTimeMs;
    }
}
