package com.sadbob.CalendarConverter.util;


import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;
import org.springframework.stereotype.Component;

@Component
public class HijriDateConverter {

    // Convert Gregorian to Hijri (Umm al-Qura)
    public HijriDate toHijri(int gregorianYear, int gregorianMonth, int gregorianDay) {
        LocalDate gregorianDate = LocalDate.of(gregorianYear, gregorianMonth, gregorianDay);
        HijrahDate hijrahDate = HijrahDate.from(gregorianDate);

        int hYear = hijrahDate.get(ChronoField.YEAR);
        int hMonth = hijrahDate.get(ChronoField.MONTH_OF_YEAR);
        int hDay = hijrahDate.get(ChronoField.DAY_OF_MONTH);

        return new HijriDate(hYear, hMonth, hDay);
    }

    // Convert Hijri to Gregorian
    public LocalDate toGregorian(int hijriYear, int hijriMonth, int hijriDay) {
        HijrahDate hijrahDate = HijrahDate.of(hijriYear, hijriMonth, hijriDay);
        return LocalDate.from(hijrahDate);
    }

    // Record to hold Hijri date
    public record HijriDate(int year, int month, int day) {}
}
