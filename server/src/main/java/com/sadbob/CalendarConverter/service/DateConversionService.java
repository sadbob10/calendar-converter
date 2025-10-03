package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.dto.responseDTO.ConversionResponse;
import com.sadbob.CalendarConverter.exception.ConversionException;
import com.sadbob.CalendarConverter.util.CalendarType;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DateConversionService {

    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    public ConversionResponse convertDate(String calendarType, String date) {
        try {
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

            return new ConversionResponse(
                    date,
                    sourceCalendar.name().toLowerCase(),
                    conversions,
                    formattedDates,
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
        // Add Ethiopian month names if needed
        return date + " (Ethiopian)";
    }

    private String formatHijriDate(String date) {
        // Add Hijri month names if needed
        return date + " (Hijri)";
    }
}
