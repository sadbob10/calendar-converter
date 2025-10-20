package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.requestDTO.AgeCalculationRequest;
import com.sadbob.CalendarConverter.dto.requestDTO.DateConversionRequest;
import com.sadbob.CalendarConverter.dto.responseDTO.AgeResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.ConversionResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.TodayResponse;
import com.sadbob.CalendarConverter.service.AgeCalculationService;
import com.sadbob.CalendarConverter.service.DateConversionService;
import com.sadbob.CalendarConverter.service.TodayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dates")
@Tag(name = "Date Converter", description = "Calendar date conversion APIs")
@CrossOrigin(origins = "*")
public class DateController {

    private final DateConversionService dateConversionService;
    private final AgeCalculationService ageCalculationService;
    private final TodayService todayService;

    public DateController(DateConversionService dateConversionService,
                          AgeCalculationService ageCalculationService,
                          TodayService todayService) {
        this.dateConversionService = dateConversionService;
        this.ageCalculationService = ageCalculationService;
        this.todayService = todayService;
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert date between calendars")
    public ResponseEntity<ConversionResponse> convertDate(@Valid @RequestBody DateConversionRequest request) {
        ConversionResponse response = dateConversionService.convertDate(
                request.calendarType(), request.date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/age")
    @Operation(summary = "Calculate age from birth date")
    public ResponseEntity<AgeResponse> calculateAge(@Valid @RequestBody AgeCalculationRequest request) {
        AgeResponse response = ageCalculationService.calculateAge(
                request.calendarType(), request.birthDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's date in all calendars")
    public ResponseEntity<TodayResponse> getToday() {
        TodayResponse response = todayService.getTodayInAllCalendars();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Calendar Converter API is running");
    }


    @PostMapping("/convert/single")
    @Operation(summary = "Convert single date (alternative endpoint)")
    public ResponseEntity<ConversionResponse> convertSingleDate(
            @Valid @RequestBody DateConversionRequest request) {
        // Your existing single conversion logic
        ConversionResponse response = dateConversionService.convertDate(
                request.calendarType(), request.date());
        return ResponseEntity.ok(response);
    }
}
