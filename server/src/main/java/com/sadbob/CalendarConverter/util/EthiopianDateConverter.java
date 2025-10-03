package com.sadbob.CalendarConverter.util;

import com.sadbob.CalendarConverter.exception.ConversionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class EthiopianDateConverter {

    // Ethiopian calendar constants
    private static final int ETHIOPIAN_YEAR_OFFSET = 8;
    private static final int ETHIOPIAN_MONTH_OFFSET = 9;
    private static final int DAYS_IN_ETHIOPIAN_MONTH = 30;

    public EthiopianDate toEthiopian(int gregorianYear, int gregorianMonth, int gregorianDay) {
        try {

            int ethiopianYear = gregorianYear - ETHIOPIAN_YEAR_OFFSET;
            if (gregorianMonth <= ETHIOPIAN_MONTH_OFFSET) {
                ethiopianYear--;
            }

            int ethiopianMonth = (gregorianMonth + 3) % 12;
            if (ethiopianMonth == 0) ethiopianMonth = 12;

            int ethiopianDay = Math.min(gregorianDay, DAYS_IN_ETHIOPIAN_MONTH);

            return new EthiopianDate(ethiopianYear, ethiopianMonth, ethiopianDay);
        } catch (Exception e) {
            log.error("Error converting Gregorian to Ethiopian date: {}-{}-{}",
                    gregorianYear, gregorianMonth, gregorianDay, e);
            throw new ConversionException("Invalid Gregorian date format or value", e);
        }
    }

    public LocalDate toGregorian(int ethiopianYear, int ethiopianMonth, int ethiopianDay) {
        try {
            // Simple conversion logic
            int gregorianYear = ethiopianYear + ETHIOPIAN_YEAR_OFFSET;
            if (ethiopianMonth <= 4) {
                gregorianYear++;
            }

            int gregorianMonth = (ethiopianMonth + 8) % 12;
            if (gregorianMonth == 0) gregorianMonth = 12;

            return LocalDate.of(gregorianYear, gregorianMonth,
                    Math.min(ethiopianDay, LocalDate.of(gregorianYear, gregorianMonth, 1).lengthOfMonth()));

        } catch (Exception e) {
            log.error("Error converting Ethiopian to Gregorian date: {}-{}-{}",
                    ethiopianYear, ethiopianMonth, ethiopianDay, e);
            throw new ConversionException("Invalid Ethiopian date format or value", e);
        }
    }

    public record EthiopianDate(int year, int month, int day) {}
}