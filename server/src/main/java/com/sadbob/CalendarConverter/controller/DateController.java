package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.requestDTO.age.AgeCalculationRequest;
import com.sadbob.CalendarConverter.dto.requestDTO.conversion.DateConversionRequest;
import com.sadbob.CalendarConverter.dto.responseDTO.age.AgeResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.conversion.ConversionResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.TodayResponse;
import com.sadbob.CalendarConverter.service.AgeCalculationService;
import com.sadbob.CalendarConverter.service.DateConversionService;
import com.sadbob.CalendarConverter.service.TodayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dates")
@Tag(name = "Date Converter", description = "APIs for single date conversion, age calculation, and today's date")
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
    @Operation(
            summary = "Convert date between calendar systems",
            description = """
            Convert a single date from one calendar system to another.
            Supported calendars: GREGORIAN, ETHIOPIAN, HIJRI.
            
            **Example Request:**
            ```json
            {
              "calendarType": "GREGORIAN",
              "date": "2024-12-25",
              "targetCalendar": "ETHIOPIAN"
            }
            ```
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Date converted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format or calendar type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ConversionResponse> convertDate(
            @Parameter(description = "Date conversion request", required = true)
            @Valid @RequestBody DateConversionRequest request) {
        ConversionResponse response = dateConversionService.convertDate(
                request.calendarType(), request.date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/age")
    @Operation(
            summary = "Calculate age from birth date",
            description = "Calculate current age based on birth date in any supported calendar system"
    )
    public ResponseEntity<AgeResponse> calculateAge(
            @Parameter(description = "Age calculation request", required = true)
            @Valid @RequestBody AgeCalculationRequest request) {
        AgeResponse response = ageCalculationService.calculateAge(
                request.calendarType(), request.birthDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today")
    @Operation(
            summary = "Get today's date in all calendar systems",
            description = "Returns current date in Gregorian, Ethiopian, and Hijri calendars"
    )
    public ResponseEntity<TodayResponse> getToday() {
        TodayResponse response = todayService.getTodayInAllCalendars();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(
            summary = "API health check",
            description = "Simple endpoint to verify the API is running",
            hidden = true // Hide from documentation if desired
    )
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
