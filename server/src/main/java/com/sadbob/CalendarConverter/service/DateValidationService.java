package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.exception.InvalidDateException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class DateValidationService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public boolean isValidDate(String dateString, String calendarType) {
        try {
            LocalDate date = LocalDate.parse(dateString, dateFormatter);

            switch (calendarType.toLowerCase()) {
                case "ethiopian":
                case "eth":
                    return isValidEthiopianDate(date);
                case "hijri":
                case "hij":
                    return isValidHijriDate(date);
                case "gregorian":
                case "greg":
                default:
                    return isValidGregorianDate(date);
            }
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidGregorianDate(LocalDate date) {
        // Gregorian dates are validated by LocalDate.parse
        // Additional validation if needed
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        // Basic range validation
        if (year < 1 || year > 9999) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;

        // Validate specific month days
        return isValidDayForMonth(year, month, day);
    }

    private boolean isValidEthiopianDate(LocalDate date) {
        // Ethiopian date validation
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        if (year < 1 || year > 9999) return false;
        if (month < 1 || month > 13) return false;
        if (day < 1 || day > 30) return false;

        // Special validation for Pagume (13th month) - 5 or 6 days depending on leap year
        if (month == 13) {
            // Ethiopian leap year: year % 4 == 3
            boolean isLeapYear = (year % 4 == 3);
            return day <= (isLeapYear ? 6 : 5);
        }

        return true;
    }

    private boolean isValidHijriDate(LocalDate date) {
        // Hijri date validation
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        if (year < 1 || year > 9999) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 30) return false;

        return true;
    }

    private boolean isValidDayForMonth(int year, int month, int day) {
        switch (month) {
            case 2: // February
                boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                return day <= (isLeapYear ? 29 : 28);
            case 4: case 6: case 9: case 11: // April, June, September, November
                return day <= 30;
            default: // January, March, May, July, August, October, December
                return day <= 31;
        }
    }

    public void validateDate(String dateString, String calendarType) {
        if (!isValidDate(dateString, calendarType)) {
            throw new InvalidDateException(
                    String.format("Invalid date '%s' for calendar type '%s'", dateString, calendarType)
            );
        }
    }

    // ADD THIS MISSING METHOD
    public void validateDateRange(String calendarType, int year, int month, int day) {
        // Create a date string from components and validate it
        String dateString = String.format("%d-%02d-%02d", year, month, day);
        validateDate(dateString, calendarType);
    }

    // OPTIONAL: Add component-based validation without creating string
    public void validateDateComponents(String calendarType, int year, int month, int day) {
        // Validate ranges first
        if (year < 1 || year > 9999) {
            throw new InvalidDateException("Year must be between 1 and 9999");
        }

        switch (calendarType.toLowerCase()) {
            case "gregorian":
            case "greg":
                if (month < 1 || month > 12) {
                    throw new InvalidDateException("Gregorian month must be between 1 and 12");
                }
                if (!isValidGregorianDay(year, month, day)) {
                    throw new InvalidDateException("Invalid day for Gregorian date");
                }
                break;

            case "ethiopian":
            case "eth":
                if (month < 1 || month > 13) {
                    throw new InvalidDateException("Ethiopian month must be between 1 and 13");
                }
                if (!isValidEthiopianDay(year, month, day)) {
                    throw new InvalidDateException("Invalid day for Ethiopian date");
                }
                break;

            case "hijri":
            case "hij":
                if (month < 1 || month > 12) {
                    throw new InvalidDateException("Hijri month must be between 1 and 12");
                }
                if (day < 1 || day > 30) {
                    throw new InvalidDateException("Hijri day must be between 1 and 30");
                }
                break;

            default:
                throw new InvalidDateException("Unknown calendar type: " + calendarType);
        }
    }

    private boolean isValidGregorianDay(int year, int month, int day) {
        if (day < 1) return false;

        switch (month) {
            case 2: // February
                boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                return day <= (isLeapYear ? 29 : 28);
            case 4: case 6: case 9: case 11: // 30-day months
                return day <= 30;
            default: // 31-day months
                return day <= 31;
        }
    }

    private boolean isValidEthiopianDay(int year, int month, int day) {
        if (day < 1) return false;

        if (month == 13) { // Pagume
            boolean isLeapYear = (year % 4 == 3);
            return day <= (isLeapYear ? 6 : 5);
        }

        return day <= 30; // All other months have 30 days
    }
}