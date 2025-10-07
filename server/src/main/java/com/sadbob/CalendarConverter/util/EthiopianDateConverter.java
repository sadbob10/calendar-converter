package com.sadbob.CalendarConverter.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class EthiopianDateConverter {

    public EthiopianDate toEthiopian(int gregorianYear, int gregorianMonth, int gregorianDay) {
        LocalDate gregorianDate = LocalDate.of(gregorianYear, gregorianMonth, gregorianDay);

        int ethiopianYear = gregorianYear - 7;
        if (gregorianMonth < 9 || (gregorianMonth == 9 && gregorianDay < 11)) {
            ethiopianYear--;
        }

        LocalDate newYear = LocalDate.of(gregorianYear, 9, 11);
        if (gregorianYear % 4 == 3) { // leap year adjustment
            newYear = LocalDate.of(gregorianYear, 9, 12);
        }

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(newYear, gregorianDate);
        if (daysBetween < 0) {
            newYear = newYear.minusYears(1);
            daysBetween = java.time.temporal.ChronoUnit.DAYS.between(newYear, gregorianDate);
            ethiopianYear--;
        }

        int ethiopianMonth = (int) (daysBetween / 30) + 1;
        int ethiopianDay = (int) (daysBetween % 30) + 1;

        return new EthiopianDate(ethiopianYear, ethiopianMonth, ethiopianDay);
    }

    // 👇 ADD THIS: Ethiopian → Gregorian
    public LocalDate toGregorian(int ethYear, int ethMonth, int ethDay) {
        // Ethiopian new year in Gregorian
        int gregorianYear = ethYear + 8;
        LocalDate newYear = LocalDate.of(gregorianYear, 9, 11);
        if (gregorianYear % 4 == 3) {
            newYear = LocalDate.of(gregorianYear, 9, 12);
        }

        // Days since Ethiopian New Year
        int daysSinceNewYear = (ethMonth - 1) * 30 + (ethDay - 1);

        return newYear.plusDays(daysSinceNewYear);
    }

    public record EthiopianDate(int year, int month, int day) {}
}
