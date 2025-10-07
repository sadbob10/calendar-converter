package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.responseDTO.CalendarResponse;
import com.sadbob.CalendarConverter.service.CalendarService;
import com.sadbob.CalendarConverter.service.EthiopianCalendarService;
import com.sadbob.CalendarConverter.service.GregorianCalendarService;
import com.sadbob.CalendarConverter.service.HijriCalendarService;
import com.sadbob.CalendarConverter.util.CalendarType;
import com.sadbob.CalendarConverter.util.CalendarTypeHelper;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calendar")
@Tag(name = "Calendar View", description = "Monthly calendar view APIs")
@CrossOrigin(origins = "*")
public class CalendarController {

    private final Map<CalendarType, CalendarService> calendarServices;
    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;

    public CalendarController(
            GregorianCalendarService gregorianService,
            EthiopianCalendarService ethiopianService,
            HijriCalendarService hijriService,
            EthiopianDateConverter ethiopianConverter,
            HijriDateConverter hijriConverter) {

        this.calendarServices = new HashMap<>();
        this.calendarServices.put(CalendarType.GREGORIAN, gregorianService);
        this.calendarServices.put(CalendarType.ETHIOPIAN, ethiopianService);
        this.calendarServices.put(CalendarType.HIJRI, hijriService);
        this.ethiopianConverter = ethiopianConverter;
        this.hijriConverter = hijriConverter;
    }

    @GetMapping("/{calendarType}")
    @Operation(summary = "Get monthly calendar view")
    public ResponseEntity<CalendarResponse> getCalendar(
            @PathVariable String calendarType,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        CalendarType type = CalendarTypeHelper.safeFromString(calendarType);
        CalendarService service = calendarServices.get(type);

        if (service == null) {
            service = calendarServices.get(CalendarType.GREGORIAN);
        }

        // Get appropriate year/month for the specific calendar type
        int targetYear;
        int targetMonth;

        if (year != null && month != null) {
            // Use provided values
            targetYear = year;
            targetMonth = month;
        } else {
            // Get current date for the specific calendar
            LocalDate today = LocalDate.now();
            if (type == CalendarType.ETHIOPIAN) {
                var ethDate = ethiopianConverter.toEthiopian(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
                targetYear = ethDate.year();
                targetMonth = ethDate.month();
            } else if (type == CalendarType.HIJRI) {
                var hijriDate = hijriConverter.toHijri(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
                targetYear = hijriDate.year();
                targetMonth = hijriDate.month();
            } else {
                // Gregorian - use as is
                targetYear = today.getYear();
                targetMonth = today.getMonthValue();
            }
        }

        CalendarResponse response = service.getCalendar(targetYear, targetMonth);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{calendarType}/previous")
    @Operation(summary = "Get previous month calendar")
    public ResponseEntity<CalendarResponse> getPreviousMonth(
            @PathVariable String calendarType,
            @RequestParam int year,
            @RequestParam int month) {

        CalendarType type = CalendarTypeHelper.safeFromString(calendarType);
        CalendarService service = calendarServices.get(type);

        CalendarResponse response = service.getPreviousMonth(year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{calendarType}/next")
    @Operation(summary = "Get next month calendar")
    public ResponseEntity<CalendarResponse> getNextMonth(
            @PathVariable String calendarType,
            @RequestParam int year,
            @RequestParam int month) {

        CalendarType type = CalendarTypeHelper.safeFromString(calendarType);
        CalendarService service = calendarServices.get(type);

        CalendarResponse response = service.getNextMonth(year, month);
        return ResponseEntity.ok(response);
    }
}