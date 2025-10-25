package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.requestDTO.calendar.CalendarRequest;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarResponse;
import com.sadbob.CalendarConverter.exception.CalendarNotFoundException;
import com.sadbob.CalendarConverter.exception.CalendarServiceException;
import com.sadbob.CalendarConverter.exception.InvalidDateException;
import com.sadbob.CalendarConverter.service.interf.CalendarService;
import com.sadbob.CalendarConverter.service.impl.EthiopianCalendarServiceImpl;
import com.sadbob.CalendarConverter.service.impl.GregorianCalendarServiceImpl;
import com.sadbob.CalendarConverter.service.impl.HijriCalendarServiceImpl;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.util.CalendarTypeHelper;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calendar")
@Tag(name = "Calendar View", description = "Monthly calendar view APIs")
@CrossOrigin(origins = "*")
@Validated
public class CalendarController {

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    private final Map<CalendarType, CalendarService> calendarServices;
    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;

    public CalendarController(
            GregorianCalendarServiceImpl gregorianService,
            EthiopianCalendarServiceImpl ethiopianService,
            HijriCalendarServiceImpl hijriService,
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
            @Valid CalendarRequest request) {

        try {
            logger.info("Getting calendar for type: {}, year: {}, month: {}",
                    calendarType, request.getYear(), request.getMonth());

            CalendarType type = CalendarTypeHelper.safeFromString(calendarType);
            CalendarService service = calendarServices.get(type);

            if (service == null) {
                throw new CalendarNotFoundException("Calendar type not supported: " + calendarType);
            }

            // Get appropriate year/month for the specific calendar type
            int targetYear;
            int targetMonth;

            // If year/month not provided in request, get current date
            if (request.getYear() == null || request.getMonth() == null) {
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
                    targetYear = today.getYear();
                    targetMonth = today.getMonthValue();
                }
            } else {
                // Use provided values
                targetYear = request.getYear();
                targetMonth = request.getMonth();

                // Validate provided values
                validateYear(targetYear);
                validateMonth(targetMonth, type);
            }

            CalendarResponse response = service.getCalendar(targetYear, targetMonth);
            logger.debug("Successfully generated calendar for {} {}-{}", calendarType, targetYear, targetMonth);

            return ResponseEntity.ok(response);

        } catch (CalendarNotFoundException | InvalidDateException e) {
            logger.warn("Calendar request error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error generating calendar for type: {}", calendarType, e);
            throw new CalendarServiceException("Failed to generate calendar: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{calendarType}/previous")
    @Operation(summary = "Get previous month calendar")
    public ResponseEntity<CalendarResponse> getPreviousMonth(
            @PathVariable String calendarType,
            @RequestParam int year,
            @RequestParam int month) {

        try {
            validateYear(year);

            CalendarType type = CalendarTypeHelper.safeFromString(calendarType);
            validateMonth(month, type);

            CalendarService service = calendarServices.get(type);

            if (service == null) {
                throw new CalendarNotFoundException("Calendar type not supported: " + calendarType);
            }

            CalendarResponse response = service.getPreviousMonth(year, month);
            logger.debug("Successfully generated previous month for {} {}-{}", calendarType, year, month);

            return ResponseEntity.ok(response);

        } catch (CalendarNotFoundException | InvalidDateException e) {
            logger.warn("Previous month request error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting previous month for {} {}-{}", calendarType, year, month, e);
            throw new CalendarServiceException("Failed to get previous month: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{calendarType}/next")
    @Operation(summary = "Get next month calendar")
    public ResponseEntity<CalendarResponse> getNextMonth(
            @PathVariable String calendarType,
            @RequestParam int year,
            @RequestParam int month) {

        try {
            validateYear(year);

            CalendarType type = CalendarTypeHelper.safeFromString(calendarType);
            validateMonth(month, type);

            CalendarService service = calendarServices.get(type);

            if (service == null) {
                throw new CalendarNotFoundException("Calendar type not supported: " + calendarType);
            }

            CalendarResponse response = service.getNextMonth(year, month);
            logger.debug("Successfully generated next month for {} {}-{}", calendarType, year, month);

            return ResponseEntity.ok(response);

        } catch (CalendarNotFoundException | InvalidDateException e) {
            logger.warn("Next month request error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting next month for {} {}-{}", calendarType, year, month, e);
            throw new CalendarServiceException("Failed to get next month: " + e.getMessage(), e);
        }
    }

    // VALIDATION METHODS
    private void validateYear(int year) {
        if (year < 1 || year > 9999) {
            throw new InvalidDateException("Year must be between 1 and 9999");
        }
    }

    private void validateMonth(int month, CalendarType calendarType) {
        int maxMonth = (calendarType == CalendarType.ETHIOPIAN) ? 13 : 12;
        if (month < 1 || month > maxMonth) {
            throw new InvalidDateException(
                    String.format("Month must be between 1 and %d for %s calendar", maxMonth, calendarType)
            );
        }
    }
}