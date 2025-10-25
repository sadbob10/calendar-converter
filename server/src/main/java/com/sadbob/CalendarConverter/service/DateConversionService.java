package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.dto.responseDTO.conversion.ConversionResponse;
import com.sadbob.CalendarConverter.exception.ConversionException;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.service.interf.HolidayService;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DateConversionService {

    private static final Logger log = LoggerFactory.getLogger(DateConversionService.class);

    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;
    private final DateValidationService dateValidationService;
    private final HolidayService holidayService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // Constructor-based injection
    public DateConversionService(EthiopianDateConverter ethiopianConverter,
                                 HijriDateConverter hijriConverter,
                                 DateValidationService dateValidationService,
                                 HolidayService holidayService) {
        this.ethiopianConverter = ethiopianConverter;
        this.hijriConverter = hijriConverter;
        this.dateValidationService = dateValidationService;
        this.holidayService = holidayService;
    }

    public ConversionResponse convertDate(String calendarType, String date) {
        try {
            // Get holiday information for source date
            List<String> sourceHolidays = holidayService.getHolidayNamesForDate(calendarType, date);

            // Validate the input date before processing
            dateValidationService.validateDate(date, calendarType);

            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            CalendarType sourceCalendar = CalendarType.fromCode(calendarType);
            Map<String, String> conversions = new HashMap<>();
            Map<String, String> formattedDates = new HashMap<>();

            switch (sourceCalendar) {
                case GREGORIAN -> {
                    conversions.put("ethiopian", convertGregorianToEthiopian(year, month, day));
                    conversions.put("hijri", convertGregorianToHijri(year, month, day));
                    conversions.put("gregorian", date);

                    formattedDates.put("gregorian", formatDate(date));
                    formattedDates.put("ethiopian", formatEthiopianDate(conversions.get("ethiopian")));
                    formattedDates.put("hijri", formatHijriDate(conversions.get("hijri")));
                }
                case ETHIOPIAN -> {
                    LocalDate gregorianDate = ethiopianConverter.toGregorian(year, month, day);
                    conversions.put("gregorian", gregorianDate.format(dateFormatter));
                    conversions.put("hijri", convertGregorianToHijri(
                            gregorianDate.getYear(), gregorianDate.getMonthValue(), gregorianDate.getDayOfMonth()));
                    conversions.put("ethiopian", date);

                    formattedDates.put("ethiopian", formatEthiopianDate(date));
                    formattedDates.put("gregorian", formatDate(conversions.get("gregorian")));
                    formattedDates.put("hijri", formatHijriDate(conversions.get("hijri")));
                }
                case HIJRI -> {
                    LocalDate gregorianDate = hijriConverter.toGregorian(year, month, day);
                    conversions.put("gregorian", gregorianDate.format(dateFormatter));
                    conversions.put("ethiopian", convertGregorianToEthiopian(
                            gregorianDate.getYear(), gregorianDate.getMonthValue(), gregorianDate.getDayOfMonth()));
                    conversions.put("hijri", date);

                    formattedDates.put("hijri", formatHijriDate(date));
                    formattedDates.put("gregorian", formatDate(conversions.get("gregorian")));
                    formattedDates.put("ethiopian", formatEthiopianDate(conversions.get("ethiopian")));
                }
            }

            // Build target calendar list
            List<String> targetCalendars = new ArrayList<>(conversions.keySet());
            targetCalendars.remove(calendarType);

            // Get holiday info for target calendars
            List<String> allTargetHolidays = new ArrayList<>();
            for (String targetCal : targetCalendars) {
                String targetDate = conversions.get(targetCal);
                List<String> targetHols = holidayService.getHolidayNamesForDate(targetCal, targetDate);
                allTargetHolidays.addAll(targetHols);
            }

            return new ConversionResponse(
                    date,
                    sourceCalendar.name().toLowerCase(),
                    conversions,
                    formattedDates,
                    targetCalendars,
                    sourceHolidays,
                    allTargetHolidays,
                    "Date converted successfully"
            );

        } catch (Exception e) {
            log.error("Error converting date: {} from calendar: {}", date, calendarType, e);
            throw new ConversionException("Failed to convert date: " + e.getMessage(), e);
        }
    }

    private String convertGregorianToEthiopian(int year, int month, int day) {
        var ethDate = ethiopianConverter.toEthiopian(year, month, day);
        return String.format("%d-%02d-%02d", ethDate.year(), ethDate.month(), ethDate.day());
    }

    private String convertGregorianToHijri(int year, int month, int day) {
        var hijriDate = hijriConverter.toHijri(year, month, day);
        return String.format("%d-%02d-%02d", hijriDate.year(), hijriDate.month(), hijriDate.day());
    }

    private String formatDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            return localDate.format(displayFormatter);
        } catch (Exception e) {
            return date;
        }
    }

    private String formatEthiopianDate(String date) {
        try {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            String[] ethiopianMonths = {
                    "Mäskäräm", "Ṭiqimt", "Ḫidar", "Taḫśaś", "Ṭirr", "Yäkatit",
                    "Mägabit", "Miyazya", "Gənbot", "Säne", "Ḥamle", "Nähäse", "Ṗagume"
            };
            String monthName = (month >= 1 && month <= 13) ? ethiopianMonths[month - 1] : "Unknown";
            return String.format("%s %d, %d (Ethiopian)", monthName, day, year);
        } catch (Exception e) {
            return date + " (Ethiopian)";
        }
    }

    private String formatHijriDate(String date) {
        try {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            String[] hijriMonths = {
                    "Muḥarram", "Ṣafar", "Rabīʿ al-Awwal", "Rabīʿ al-Thānī",
                    "Jumādā al-Ūlā", "Jumādā al-Thāniya", "Rajab", "Shaʿbān",
                    "Ramaḍān", "Shawwāl", "Dhū al-Qaʿda", "Dhū al-Ḥijja"
            };
            String monthName = (month >= 1 && month <= 12) ? hijriMonths[month - 1] : "Unknown";
            return String.format("%s %d, %d (Hijri)", monthName, day, year);
        } catch (Exception e) {
            return date + " (Hijri)";
        }
    }
}
