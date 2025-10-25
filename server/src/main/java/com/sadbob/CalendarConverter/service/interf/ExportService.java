package com.sadbob.CalendarConverter.service.interf;

public interface ExportService {
    String exportHolidaysJson(String calendarType, Integer year);
    String exportCalendarDataJson(String calendarType, int year, int month);
    byte[] exportHolidaysCsv(String calendarType, Integer year);
    byte[] exportCalendarDataCsv(String calendarType, int year, int month);
}
