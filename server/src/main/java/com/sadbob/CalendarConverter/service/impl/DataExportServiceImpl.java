package com.sadbob.CalendarConverter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sadbob.CalendarConverter.dto.responseDTO.export.CalendarDataResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.export.HolidayDataResponse;
import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.service.interf.ExportService;
import com.sadbob.CalendarConverter.service.interf.HolidayService;
import com.sadbob.CalendarConverter.util.CalendarMonthUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataExportServiceImpl implements ExportService {

    private final HolidayService holidayService;
    private final ObjectMapper objectMapper;
    private final CalendarMonthUtils monthUtils;

    public DataExportServiceImpl(HolidayService holidayService, CalendarMonthUtils monthUtils) {
        this.holidayService = holidayService;
        this.monthUtils = monthUtils;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String exportHolidaysJson(String calendarType, Integer year) {
        try {
            List<HolidayDataResponse> holidays = getHolidaysForExport(calendarType, year);
            return objectMapper.writeValueAsString(holidays);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public String exportCalendarDataJson(String calendarType, int year, int month) {
        try {
            List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);

            CalendarDataResponse calendarData = new CalendarDataResponse(
                    calendarType,
                    year,
                    month,
                    monthUtils.getMonthName(month, calendarType),
                    holidays.stream()
                            .map(this::convertToHolidayData)
                            .collect(Collectors.toList())
            );

            return objectMapper.writeValueAsString(calendarData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] exportHolidaysCsv(String calendarType, Integer year) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<HolidayDataResponse> holidays = getHolidaysForExport(calendarType, year);

            String header = "Date,Name,Description,Type,Country,Recurring\n";
            outputStream.write(header.getBytes());

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

    @Override
    public byte[] exportCalendarDataCsv(String calendarType, int year, int month) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);

            String header = "Month,Day,Name,Description,Type,Country\n";
            outputStream.write(header.getBytes());

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

    private List<HolidayDataResponse> getHolidaysForExport(String calendarType, Integer year) {
        List<HolidayDataResponse> allHolidays = new ArrayList<>();

        if (year != null) {
            for (int month = 1; month <= 13; month++) {
                List<Holiday> monthlyHolidays = holidayService.getHolidaysForMonth(calendarType, year, month);
                allHolidays.addAll(monthlyHolidays.stream()
                        .map(this::convertToHolidayData)
                        .collect(Collectors.toList()));
            }
        } else {
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
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}