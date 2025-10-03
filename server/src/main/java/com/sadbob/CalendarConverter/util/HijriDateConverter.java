package com.sadbob.CalendarConverter.util;

import com.sadbob.CalendarConverter.exception.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class HijriDateConverter {

    private static final Logger log = LoggerFactory.getLogger(HijriDateConverter.class);

    // Simple conversion constants (consider using a library like Umm al-Qura)
    private static final double HIJRI_TO_GREGORIAN_RATIO = 0.970224;
    private static final int HIJRI_EPOCH = 622;

    public HijriDate toHijri(int gregorianYear, int gregorianMonth, int gregorianDay) {
        try {
            LocalDate gregorianDate = LocalDate.of(gregorianYear, gregorianMonth, gregorianDay);
            LocalDate epoch = LocalDate.of(HIJRI_EPOCH, 7, 16);

            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(epoch, gregorianDate);
            int hijriYear = (int) (daysBetween * HIJRI_TO_GREGORIAN_RATIO / 354.367);
            int hijriMonth = (int) ((daysBetween * HIJRI_TO_GREGORIAN_RATIO) % 354.367 / 29.53) + 1;
            int hijriDay = (int) ((daysBetween * HIJRI_TO_GREGORIAN_RATIO) % 29.53) + 1;

            // Normalize
            if (hijriDay > 30) hijriDay = 30;
            if (hijriMonth > 12) {
                hijriMonth = 1;
                hijriYear++;
            }

            return new HijriDate(hijriYear, hijriMonth, hijriDay);
        } catch (Exception e) {
            log.error("Error converting Gregorian to Hijri date: {}-{}-{}",
                    gregorianYear, gregorianMonth, gregorianDay, e);
            throw new ConversionException("Invalid Gregorian date format or value", e);
        }
    }

    public LocalDate toGregorian(int hijriYear, int hijriMonth, int hijriDay) {
        try {
            // Simple reverse calculation
            double totalDays = hijriYear * 354.367 + (hijriMonth - 1) * 29.53 + (hijriDay - 1);
            totalDays /= HIJRI_TO_GREGORIAN_RATIO;

            LocalDate epoch = LocalDate.of(HIJRI_EPOCH, 7, 16);
            return epoch.plusDays((long) totalDays); // Removed redundant variable

        } catch (Exception e) {
            log.error("Error converting Hijri to Gregorian date: {}-{}-{}",
                    hijriYear, hijriMonth, hijriDay, e);
            throw new ConversionException("Invalid Hijri date format or value", e);
        }
    }

    public record HijriDate(int year, int month, int day) {}
}