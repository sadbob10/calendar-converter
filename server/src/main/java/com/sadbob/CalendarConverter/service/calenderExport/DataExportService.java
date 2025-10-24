package com.sadbob.CalendarConverter.service.calenderExport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sadbob.CalendarConverter.dto.responseDTO.export.CalendarDataResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.export.HolidayDataResponse;
import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.service.HolidayService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataExportService {

    private final HolidayService holidayService;
    private final ObjectMapper objectMapper;

    public DataExportService(HolidayService holidayService) {
        this.holidayService = holidayService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // JSON Export Methods
    public String exportHolidaysJson(String calendarType, Integer year) {
        try {
            List<HolidayDataResponse> holidays = getHolidaysForExport(calendarType, year);
            return objectMapper.writeValueAsString(holidays);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON: " + e.getMessage(), e);
        }
    }

    public String exportCalendarDataJson(String calendarType, int year, int month) {
        try {
            List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);

            CalendarDataResponse calendarData = new CalendarDataResponse(
                    calendarType,
                    year,
                    month,
                    getMonthName(month, calendarType),
                    holidays.stream()
                            .map(this::convertToHolidayData)
                            .collect(Collectors.toList())
            );

            return objectMapper.writeValueAsString(calendarData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON: " + e.getMessage(), e);
        }
    }

    // CSV Export Methods
    public byte[] exportHolidaysCsv(String calendarType, Integer year) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<HolidayDataResponse> holidays = getHolidaysForExport(calendarType, year);

            // CSV Header
            String header = "Date,Name,Description,Type,Country,Recurring\n";
            outputStream.write(header.getBytes());

            // CSV Rows
            for (HolidayDataResponse holiday : holidays) {
                String row = String.format("%s,%s,%s,%s,%s,%s\n",
                        holiday.getDate(),
                        escapeCsv(holiday.getName()),
                        escapeCsv(holiday.getDescription()),
                        holiday.getType(),
                        holiday.getCountryCode() != null ? holiday.getCountryCode() : "",
                        holiday.isRecurring()
                );
                outputStream.write(row.getBytes());
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CSV: " + e.getMessage(), e);
        }
    }

    public byte[] exportCalendarDataCsv(String calendarType, int year, int month) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);

            // CSV Header
            String header = "Month,Day,Name,Description,Type,Country\n";
            outputStream.write(header.getBytes());

            // CSV Rows
            for (Holiday holiday : holidays) {
                String row = String.format("%d,%d,%s,%s,%s,%s\n",
                        holiday.getMonthNumber(),
                        holiday.getDayOfMonth(),
                        escapeCsv(holiday.getName()),
                        escapeCsv(holiday.getDescription()),
                        holiday.getHolidayType().name(),
                        holiday.getCountryCode() != null ? holiday.getCountryCode() : ""
                );
                outputStream.write(row.getBytes());
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CSV: " + e.getMessage(), e);
        }
    }

    // Helper Methods
    private List<HolidayDataResponse> getHolidaysForExport(String calendarType, Integer year) {
        List<HolidayDataResponse> allHolidays = List.of();

        if (year != null) {
            // Get holidays for specific year
            for (int month = 1; month <= 13; month++) { // Include month 13 for Ethiopian
                List<Holiday> monthlyHolidays = holidayService.getHolidaysForMonth(calendarType, year, month);
                allHolidays.addAll(monthlyHolidays.stream()
                        .map(this::convertToHolidayData)
                        .collect(Collectors.toList()));
            }
        } else {
            // Get all holidays for the calendar type (current year as sample)
            int currentYear = LocalDate.now().getYear();
            for (int month = 1; month <= 13; month++) {
                List<Holiday> monthlyHolidays = holidayService.getHolidaysForMonth(calendarType, currentYear, month);
                allHolidays.addAll(monthlyHolidays.stream()
                        .map(this::convertToHolidayData)
                        .collect(Collectors.toList()));
            }
        }

        return allHolidays;
    }

    private HolidayDataResponse convertToHolidayData(Holiday holiday) {
        String date = String.format("%d-%02d-%02d",
                holiday.getSpecificYear() != null ? holiday.getSpecificYear() : LocalDate.now().getYear(),
                holiday.getMonthNumber(),
                holiday.getDayOfMonth()
        );

        return new HolidayDataResponse(
                date,
                holiday.getName(),
                holiday.getDescription(),
                holiday.getHolidayType().name(),
                holiday.getCountryCode(),
                holiday.getIsRecurring()
        );
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        // Escape commas and quotes for CSV
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String getMonthName(int month, String calendarType) {
        return getString(month, calendarType);
    }

    static String getString(int month, String calendarType) {
        String[] gregorianMonths = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String[] ethiopianMonths = {"Mäskäräm", "Ṭiqimt", "Ḫidar", "Taḫśaś", "Ṭirr", "Yäkatit",
                "Mägabit", "Miyazya", "Gənbot", "Säne", "Ḥamle", "Nähäse", "Ṗagume"};
        String[] hijriMonths = {"Muḥarram", "Ṣafar", "Rabīʿ al-Awwal", "Rabīʿ al-Thānī",
                "Jumādā al-Ūlā", "Jumādā al-Thāniya", "Rajab", "Shaʿbān",
                "Ramaḍān", "Shawwāl", "Dhū al-Qaʿda", "Dhū al-Ḥijja"};

        if ("ethiopian".equalsIgnoreCase(calendarType) && month <= 13) {
            return ethiopianMonths[month - 1];
        } else if ("hijri".equalsIgnoreCase(calendarType) && month <= 12) {
            return hijriMonths[month - 1];
        } else if (month <= 12) {
            return gregorianMonths[month - 1];
        }
        return "Month " + month;
    }
}
