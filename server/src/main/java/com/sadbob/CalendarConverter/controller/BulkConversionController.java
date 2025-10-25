package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.requestDTO.conversion.BulkConversionRequest;
import com.sadbob.CalendarConverter.dto.requestDTO.conversion.DateRangeRequest;
import com.sadbob.CalendarConverter.dto.responseDTO.conversion.BulkConversionResponse;
import com.sadbob.CalendarConverter.service.BulkConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/bulk")
@Tag(name = "Bulk Conversion", description = "Bulk date conversion APIs")
@CrossOrigin(origins = "*")
public class BulkConversionController {

    private final BulkConversionService bulkConversionService;

    public BulkConversionController(BulkConversionService bulkConversionService) {
        this.bulkConversionService = bulkConversionService;
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert multiple dates in one request")
    public ResponseEntity<BulkConversionResponse> convertBulkDates(
            @Valid @RequestBody BulkConversionRequest request) {

        // Limit batch size for performance
        if (request.getConversions().size() > 100) {
            return ResponseEntity.badRequest().body(createErrorResponse(
            ));
        }

        BulkConversionResponse response = bulkConversionService.convertBulkDates(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert/async")
    @Operation(summary = "Convert multiple dates asynchronously")
    public CompletableFuture<ResponseEntity<BulkConversionResponse>> convertBulkDatesAsync(
            @Valid @RequestBody BulkConversionRequest request) {

        if (request.getConversions().size() > 100) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(
                    createErrorResponse()));
        }

        return bulkConversionService.convertBulkDatesAsync(request)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/convert/range")
    @Operation(summary = "Convert a range of dates")
    public ResponseEntity<BulkConversionResponse> convertDateRange(
            @Valid @RequestBody DateRangeRequest request) {

        BulkConversionResponse response = bulkConversionService.convertDateRange(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert/optimized")
    @Operation(summary = "Optimized bulk conversion for same source calendar")
    public ResponseEntity<BulkConversionResponse> convertBulkOptimized(
            @Valid @RequestBody BulkConversionRequest request) {

        if (request.getConversions().size() > 100) {
            return ResponseEntity.badRequest().body(createErrorResponse(
            ));
        }

        BulkConversionResponse response = bulkConversionService.convertBatchSameSource(request);
        return ResponseEntity.ok(response);
    }

    private BulkConversionResponse createErrorResponse() {
        BulkConversionResponse.BulkSummary summary =
                new BulkConversionResponse.BulkSummary(0, 0, 0, 0);
        return new BulkConversionResponse(null, summary, "Too many conversions. Maximum 100 conversions per request.");
    }
}