package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.dto.requestDTO.BulkConversionRequest;
import com.sadbob.CalendarConverter.dto.requestDTO.DateRangeRequest;
import com.sadbob.CalendarConverter.dto.responseDTO.BulkConversionResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.ConversionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BulkConversionService {

    private static final Logger log = LoggerFactory.getLogger(BulkConversionService.class);

    private final DateConversionService dateConversionService;
    private final DateValidationService dateValidationService;

    public BulkConversionService(DateConversionService dateConversionService, DateValidationService dateValidationService) {
        this.dateConversionService = dateConversionService;
        this.dateValidationService = dateValidationService;
    }

    public BulkConversionResponse convertBulkDates(BulkConversionRequest request) {
        long startTime = System.currentTimeMillis();

        List<BulkConversionResponse.SingleConversionResult> results = new ArrayList<>();

        for (BulkConversionRequest.SingleConversionRequest singleRequest : request.getConversions()) {
            BulkConversionResponse.SingleConversionResult result = convertSingleDate(singleRequest);
            results.add(result);
        }

        long processingTime = System.currentTimeMillis() - startTime;

        BulkConversionResponse.BulkSummary summary = createSummary(results, processingTime);
        String message = generateSummaryMessage(summary);

        return new BulkConversionResponse(results, summary, message);
    }

    private BulkConversionResponse.SingleConversionResult convertSingleDate(
            BulkConversionRequest.SingleConversionRequest request) {

        try {
            // Validate input
            dateValidationService.validateDate(request.getDate(), request.getSourceCalendar());

            // Perform conversion using existing service
            ConversionResponse conversionResponse = dateConversionService.convertDate(
                    request.getSourceCalendar(),
                    request.getDate()
            );

            // Extract the specific target calendar conversion
            String targetDate = conversionResponse.conversions().get(request.getTargetCalendar());
            String formattedDate = conversionResponse.formattedDates().get(request.getTargetCalendar());

            if (targetDate == null) {
                throw new IllegalArgumentException("Target calendar not found: " + request.getTargetCalendar());
            }

            return new BulkConversionResponse.SingleConversionResult(
                    request.getDate(),
                    request.getSourceCalendar(),
                    targetDate,
                    request.getTargetCalendar(),
                    formattedDate,
                    true,
                    null
            );

        } catch (Exception e) {
            log.warn("Bulk conversion failed for {} from {} to {}: {}",
                    request.getDate(), request.getSourceCalendar(), request.getTargetCalendar(), e.getMessage());

            return new BulkConversionResponse.SingleConversionResult(
                    request.getDate(),
                    request.getSourceCalendar(),
                    null,
                    request.getTargetCalendar(),
                    null,
                    false,
                    e.getMessage()
            );
        }
    }

    // Async version for better performance with large batches
    public CompletableFuture<BulkConversionResponse> convertBulkDatesAsync(BulkConversionRequest request) {
        return CompletableFuture.supplyAsync(() -> convertBulkDates(request));
    }

    // Optimized batch conversion for same source calendar
    public BulkConversionResponse convertBatchSameSource(BulkConversionRequest request) {
        long startTime = System.currentTimeMillis();

        if (request.getConversions().isEmpty()) {
            return createEmptyResponse();
        }

        // Check if all requests have same source calendar (optimization)
        String firstSourceCalendar = request.getConversions().get(0).getSourceCalendar();
        boolean sameSourceCalendar = request.getConversions().stream()
                .allMatch(req -> req.getSourceCalendar().equals(firstSourceCalendar));

        List<BulkConversionResponse.SingleConversionResult> results;

        if (sameSourceCalendar) {
            // Optimized path for same source calendar
            results = convertBatchOptimized(request, firstSourceCalendar);
        } else {
            // General path for mixed source calendars
            results = request.getConversions().stream()
                    .map(this::convertSingleDate)
                    .collect(Collectors.toList());
        }

        long processingTime = System.currentTimeMillis() - startTime;
        BulkConversionResponse.BulkSummary summary = createSummary(results, processingTime);
        String message = generateSummaryMessage(summary);

        return new BulkConversionResponse(results, summary, message);
    }

    private List<BulkConversionResponse.SingleConversionResult> convertBatchOptimized(
            BulkConversionRequest request, String sourceCalendar) {

        return request.getConversions().stream()
                .map(this::convertSingleDate)
                .collect(Collectors.toList());
    }

    private BulkConversionResponse.BulkSummary createSummary(
            List<BulkConversionResponse.SingleConversionResult> results, long processingTime) {

        int total = results.size();
        int successful = (int) results.stream().filter(BulkConversionResponse.SingleConversionResult::isSuccess).count();
        int failed = total - successful;

        return new BulkConversionResponse.BulkSummary(total, successful, failed, processingTime);
    }

    private String generateSummaryMessage(BulkConversionResponse.BulkSummary summary) {
        if (summary.getFailedConversions() == 0) {
            return String.format("All %d conversions completed successfully in %d ms",
                    summary.getSuccessfulConversions(), summary.getProcessingTimeMs());
        } else {
            return String.format("Completed %d/%d conversions (%d failed) in %d ms",
                    summary.getSuccessfulConversions(), summary.getTotalRequests(),
                    summary.getFailedConversions(), summary.getProcessingTimeMs());
        }
    }

    private BulkConversionResponse createEmptyResponse() {
        BulkConversionResponse.BulkSummary summary = new BulkConversionResponse.BulkSummary(0, 0, 0, 0);
        return new BulkConversionResponse(List.of(), summary, "No conversion requests provided");
    }

    public BulkConversionResponse convertDateRange(DateRangeRequest request) {
        if (!request.isValidRange()) {
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }

        if (request.getTotalDays() > 1000) {
            throw new IllegalArgumentException("Date range too large. Maximum 1000 days allowed.");
        }

        long startTime = System.currentTimeMillis();
        List<BulkConversionResponse.SingleConversionResult> results = new ArrayList<>();

        LocalDate currentDate = request.getStartDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!currentDate.isAfter(request.getEndDate())) {
            // Skip weekends if requested
            if (request.getIncludeWeekends() || !isWeekend(currentDate)) {
                BulkConversionRequest.SingleConversionRequest singleRequest =
                        new BulkConversionRequest.SingleConversionRequest();
                singleRequest.setDate(currentDate.format(formatter));
                singleRequest.setSourceCalendar(request.getSourceCalendar());
                singleRequest.setTargetCalendar(request.getTargetCalendar());

                results.add(convertSingleDate(singleRequest));
            }

            currentDate = currentDate.plusDays(request.getStepDays());
        }

        long processingTime = System.currentTimeMillis() - startTime;
        BulkConversionResponse.BulkSummary summary = createSummary(results, processingTime);
        String message = generateRangeSummaryMessage(summary, request);

        return new BulkConversionResponse(results, summary, message);
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() >= 6; // Saturday or Sunday
    }

    private String generateRangeSummaryMessage(BulkConversionResponse.BulkSummary summary, DateRangeRequest request) {
        return String.format("Converted %d dates from %s to %s. %d successful, %d failed in %d ms",
                summary.getTotalRequests(),
                request.getStartDate(),
                request.getEndDate(),
                summary.getSuccessfulConversions(),
                summary.getFailedConversions(),
                summary.getProcessingTimeMs());
    }
}