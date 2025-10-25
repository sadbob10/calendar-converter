package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.service.interf.HolidayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ICalExportService {

    private final HolidayService holidayService;

    public ICalExportService(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public String generateHolidaysICS(String calendarType, int year) {
        StringBuilder icsContent = new StringBuilder();

        // ICS Header
        icsContent.append("BEGIN:VCALENDAR\r\n");
        icsContent.append("VERSION:2.0\r\n");
        icsContent.append("PRODID:-//Sadbob//Calendar Converter//EN\r\n");
        icsContent.append("CALSCALE:GREGORIAN\r\n");
        icsContent.append("METHOD:PUBLISH\r\n");

        // Add holidays for each month
        for (int month = 1; month <= 12; month++) {
            List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);
            for (Holiday holiday : holidays) {
                icsContent.append(createHolidayEvent(holiday, year));
            }
        }

        // ICS Footer
        icsContent.append("END:VCALENDAR\r\n");

        return icsContent.toString();
    }

    private String createHolidayEvent(Holiday holiday, int year) {
        // Create date for the holiday
        LocalDate holidayDate = LocalDate.of(
                holiday.getSpecificYear() != null ? holiday.getSpecificYear() : year,
                holiday.getMonthNumber(),
                holiday.getDayOfMonth()
        );

        // Format dates in ICS format (YYYYMMDD)
        String startDate = holidayDate.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        String endDate = holidayDate.plusDays(1).format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);

        // Generate unique ID
        String uid = java.util.UUID.randomUUID().toString();

        StringBuilder event = new StringBuilder();
        event.append("BEGIN:VEVENT\r\n");
        event.append("UID:").append(uid).append("\r\n");
        event.append("DTSTAMP:").append(getCurrentTimestamp()).append("\r\n");
        event.append("DTSTART;VALUE=DATE:").append(startDate).append("\r\n");
        event.append("DTEND;VALUE=DATE:").append(endDate).append("\r\n");
        event.append("SUMMARY:").append(escapeICS(holiday.getName())).append("\r\n");
        event.append("DESCRIPTION:").append(escapeICS(holiday.getDescription())).append("\r\n");
        event.append("CATEGORIES:HOLIDAY\r\n");
        event.append("CLASS:PUBLIC\r\n");
        event.append("TRANSP:TRANSPARENT\r\n"); // Mark as free time
        event.append("END:VEVENT\r\n");

        return event.toString();
    }

    private String getCurrentTimestamp() {
        return java.time.ZonedDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
    }

    private String escapeICS(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace(",", "\\,")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}