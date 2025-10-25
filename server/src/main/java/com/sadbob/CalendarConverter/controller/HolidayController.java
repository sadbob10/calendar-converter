package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.responseDTO.holidays.HolidayCheckResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.holidays.HolidayResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.holidays.MonthHolidaysResponse;
import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.service.interf.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/holidays")
@Tag(name = "Holidays", description = "Calendar holiday information APIs")
@CrossOrigin(origins = "*")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/check/{calendarType}/{date}")
    @Operation(summary = "Check if a specific date is a holiday")
    public ResponseEntity<HolidayCheckResponse> checkHoliday(
            @PathVariable String calendarType,
            @PathVariable String date) {

        return getHolidayCheckResponseResponseEntity(calendarType, date);
    }

    @GetMapping("/month/{calendarType}/{year}/{month}")
    @Operation(summary = "Get all holidays for a specific month")
    public ResponseEntity<MonthHolidaysResponse> getMonthHolidays(
            @PathVariable String calendarType,
            @PathVariable int year,
            @PathVariable int month) {

        List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);
        List<HolidayResponse> holidayResponses = holidays.stream()
                .map(this::convertToHolidayResponse)
                .collect(Collectors.toList());

        MonthHolidaysResponse response = new MonthHolidaysResponse(
                year, month, calendarType, holidayResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/upcoming/{calendarType}")
    @Operation(summary = "Get upcoming holidays for a calendar")
    public ResponseEntity<List<HolidayResponse>> getUpcomingHolidays(
            @PathVariable String calendarType,
            @RequestParam(defaultValue = "1") int currentMonth,
            @RequestParam(defaultValue = "1") int currentDay) {

        List<Holiday> holidays = holidayService.getUpcomingHolidays(calendarType, currentMonth, currentDay);
        List<HolidayResponse> responses = holidays.stream()
                .map(this::convertToHolidayResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{calendarType}/{holidayType}")
    @Operation(summary = "Get holidays by type (religious, national, etc.)")
    public ResponseEntity<List<HolidayResponse>> getHolidaysByType(
            @PathVariable String calendarType,
            @PathVariable String holidayType) {

        List<Holiday> holidays = holidayService.getHolidaysByType(calendarType, holidayType);
        List<HolidayResponse> responses = holidays.stream()
                .map(this::convertToHolidayResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/today/{calendarType}")
    @Operation(summary = "Check if today is a holiday in the specified calendar")
    public ResponseEntity<HolidayCheckResponse> checkTodayHoliday(
            @PathVariable String calendarType) {

        LocalDate today = LocalDate.now();
        String todayStr = today.toString();

        return getHolidayCheckResponseResponseEntity(calendarType, todayStr);
    }

    private ResponseEntity<HolidayCheckResponse> getHolidayCheckResponseResponseEntity(@PathVariable String calendarType, String todayStr) {
        List<Holiday> holidays = holidayService.getHolidaysForDate(calendarType, todayStr);
        boolean isHoliday = !holidays.isEmpty();

        String holidayName = null;
        String holidayType = null;

        if (isHoliday) {  // Remove the redundant !holidays.isEmpty() check
            Holiday holiday = holidays.get(0);
            holidayName = holiday.getName();
            holidayType = holiday.getHolidayType().name();
        }

        HolidayCheckResponse response = new HolidayCheckResponse(
                todayStr, calendarType, isHoliday, holidayName, holidayType
        );

        return ResponseEntity.ok(response);
    }

    private HolidayResponse convertToHolidayResponse(Holiday holiday) {
        String date = String.format("%04d-%02d-%02d",
                holiday.getSpecificYear() != null ? holiday.getSpecificYear() : 2023, // Sample year
                holiday.getMonthNumber(),
                holiday.getDayOfMonth()
        );

        return new HolidayResponse(
                holiday.getName(),
                holiday.getDescription(),
                holiday.getCalendarType(),
                holiday.getHolidayType(),
                date,
                holiday.getCountryCode(),
                holiday.getIsRecurring()
        );
    }
}